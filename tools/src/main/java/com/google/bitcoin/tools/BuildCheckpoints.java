package com.google.bitcoin.tools;

import com.google.bitcoin.core.*;
import com.google.bitcoin.params.MainNetParams;
import com.google.bitcoin.store.BlockStore;
import com.google.bitcoin.store.MemoryBlockStore;
import com.google.bitcoin.utils.BriefLogFormatter;
import com.google.bitcoin.utils.Threading;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.util.Date;
import java.util.TreeMap;

import static com.google.common.base.Preconditions.checkState;

/**
 * Downloads and verifies a full chain from your local peer, emitting checkpoints at each difficulty transition period
 * to a file which is then signed with your key.
 */
public class BuildCheckpoints {
    public static void main(String[] args) throws Exception {
        BriefLogFormatter.init();
        final NetworkParameters params = MainNetParams.get();

        // Sorted map of UNIX time of block to StoredBlock object.
        final TreeMap<Integer, StoredBlock> checkpoints = new TreeMap<Integer, StoredBlock>();

        // Configure bitcoinj to fetch only headers, not save them to disk, connect to a local fully synced/validated
        // node and to save block headers that are on interval boundaries, as long as they are <1 month old.
        final BlockStore store = new MemoryBlockStore(params);
        final BlockChain chain = new BlockChain(params, store);
        final PeerGroup peerGroup = new PeerGroup(params, chain);

        peerGroup.addAddress(InetAddress.getLocalHost());
        long now = new Date().getTime() / 1000;
        peerGroup.setFastCatchupTimeSecs(now);

        final long oneMonthAgo = now - (86400* 2);  // start 2 days ago

        chain.addListener(new AbstractBlockChainListener() {
            @Override
            public void notifyNewBestBlock(StoredBlock block) throws VerificationException {
                int height = block.getHeight();
                if (height % CoinDefinition.getIntervalForCheckpoints(height, false) == 0 &&
                        block.getHeader().getTimeSeconds() <= oneMonthAgo) {
                    System.out.println(String.format("Checkpointing block %s at height %d: diff %08x",
                            block.getHeader().getHash(), block.getHeight(), block.getHeader().getDifficultyTarget()));
                    checkpoints.put(height, block);
                }
            }
        }, Threading.SAME_THREAD);

        peerGroup.startAndWait();
        peerGroup.downloadBlockChain();

        checkState(checkpoints.size() > 0);

        // Write checkpoint data out.
        final FileOutputStream fileOutputStream = new FileOutputStream("checkpoints", false);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        final DigestOutputStream digestOutputStream = new DigestOutputStream(fileOutputStream, digest);
        digestOutputStream.on(false);
        final DataOutputStream dataOutputStream = new DataOutputStream(digestOutputStream);
        dataOutputStream.writeBytes("CHECKPOINTS 1");
        dataOutputStream.writeInt(0);  // Number of signatures to read. Do this later.
        digestOutputStream.on(true);
        dataOutputStream.writeInt(checkpoints.size());
        ByteBuffer buffer = ByteBuffer.allocate(StoredBlock.COMPACT_SERIALIZED_SIZE);
        int i = 0;
        for (StoredBlock block : checkpoints.values()) {
            block.serializeCompact(buffer);
            dataOutputStream.write(buffer.array());
            buffer.position(0);
            ++i;
            System.out.println("writing checkpoint " + i);
        }
        dataOutputStream.close();
        Sha256Hash checkpointsHash = new Sha256Hash(digest.digest());
        System.out.println("Hash of checkpoints data is " + checkpointsHash);
        digestOutputStream.close();
        fileOutputStream.close();

        peerGroup.stopAndWait();
        store.close();

        // Sanity check the created file.
        CheckpointManager manager = new CheckpointManager(params, new FileInputStream("checkpoints"));
        checkState(manager.numCheckpoints() == checkpoints.size());
        StoredBlock test = manager.getCheckpointBefore(1517084258);  // Timestamp; Just after block 802100 below plus approx. 200 blocks = 802250 with time 2018-01-27 20:17:38 = 1517084258
        checkState(test.getHeight() == 802100); // values should less than above; block 802100 time = 2018-01-26 14:18:00 < 2018-01-27 20:17:38
        checkState(test.getHeader().getHashAsString().equals("b64e5c9be39e775d3696baf678ddb461fd9e435f78c58c556cd90b195d13dd4d"));

    }
}
