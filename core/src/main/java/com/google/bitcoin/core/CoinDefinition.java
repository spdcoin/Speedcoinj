package com.google.bitcoin.core;

import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import java.util.Vector;

public class CoinDefinition {


    public static final String coinName = "Speedcoin";
    public static final String coinTicker = "SPD";
    public static final String coinURIScheme = "speedcoin";
    public static final String cryptsyMarketId = "";
    public static final String cryptsyMarketCurrency = "BTC";


    public static final String BLOCKEXPLORER_BASE_URL_PROD = "http://speedcoin.org:2750/";
    public static final String BLOCKEXPLORER_BASE_URL_TEST = "http://speedcoin.org:2750/";

    public static final String DONATION_ADDRESS = "SZSsPFnanuNiBLYK39JL3gjj4gxh32j43h";  // donation SPD address

    enum CoinHash {
        SHA256,
        scrypt
    };
    public static final CoinHash coinHash = CoinHash.scrypt;
    //Original Values
    public static final int TARGET_TIMESPAN_0 = (int)(24 * 60 * 60);  // 1 day per difficulty cycle, on average.
    public static final int TARGET_SPACING_0 = (int)(60);  // 1 minute per block.
    public static final int INTERVAL_0 = TARGET_TIMESPAN_0 / TARGET_SPACING_0; // 1440 blocks

    public static final int TARGET_TIMESPAN = (int)(24 * 60 * 60);  // 1 day per difficulty cycle, on average.
    public static final int TARGET_SPACING = (int)(60);  // 1 minute per block.
    public static final int INTERVAL = TARGET_TIMESPAN / TARGET_SPACING; 
    
    public static final int TARGET_TIMESPAN_3 = (int)(24 * 60 * 60);  // 1 day per difficulty cycle, on average.
    public static final int TARGET_SPACING_3 = (int)(60);  // 1 minute per block.
    public static final int INTERVAL_3 = TARGET_TIMESPAN_0 / TARGET_SPACING_0;

    static final long nTargetSpacing = 60; // 1 minute
    static final long nOriginalInterval = 1440;
    static final long nFilteredInterval =    1440;
    static final long nOriginalTargetTimespan = nOriginalInterval * nTargetSpacing; // 1 day
    static final long nFilteredTargetTimespan = nFilteredInterval * nTargetSpacing; // 1 minute

    public static int DIFF_FILTER_THRESHOLD_TESTNET = 2;
    public static int DIFF_FILTER_THRESHOLD = 2;

    public static int nDifficultySwitchHeight = 2;
    public static int nDifficultySwitchHeightTwo = 9999999;

    public static final int getInterval(int height, boolean testNet) {
        if(height < nDifficultySwitchHeight)
            return (int)nOriginalInterval;    //1080
        else if(height < nDifficultySwitchHeightTwo)
            return (int)nFilteredInterval;      //108
        else return INTERVAL_3;
    }
    public static final int getIntervalForCheckpoints(int height, boolean testNet) {
        if(height < 8050)
            return (int)nOriginalInterval;    //1440
        else if(height < nDifficultySwitchHeightTwo)
            return (int)nOriginalInterval;      //1440
        else return (int)nOriginalInterval / 4; //360
    }
    public static final int getTargetTimespan(int height, boolean testNet) {
        if(height < nDifficultySwitchHeight)
            return TARGET_TIMESPAN_0;  
        else
            return TARGET_TIMESPAN;    
    }
    public static int getMaxTimeSpan(int value, int height, boolean testNet)
    {
        if(height < nDifficultySwitchHeight)
            return value * 4;
        else
            return value * 1;   // not used
    }
    public static int getMinTimeSpan(int value, int height, boolean testNet)
    {
        if(height < nDifficultySwitchHeight)
            return value / 4;
        else
            return value * 1;    //not used
    }
    public static int spendableCoinbaseDepth = 100; 								 //main.h: static const int COINBASE_MATURITY
    public static final String MAX_MONEY_STRING = "10105000000";     				 //main.h:  MAX_MONEY
    public static final BigInteger MAX_MONEY = new BigInteger(MAX_MONEY_STRING, 10); //main.h:  MAX_MONEY

    public static final BigInteger DEFAULT_MIN_TX_FEE = BigInteger.valueOf(1000000);   // MIN_TX_FEE - DUST_SOFT_LIMIT
    public static final BigInteger DUST_LIMIT = Utils.CENT; //main.h CTransaction::GetMinFee        0.01 coins

    public static final int PROTOCOL_VERSION = 70002;          //version.h PROTOCOL_VERSION
    public static final int MIN_PROTOCOL_VERSION = 209;        //version.h MIN_PROTO_VERSION

    public static final boolean supportsBloomFiltering = true; //Requires PROTOCOL_VERSION 70000 in the client

    public static final int Port    = 24777;       //protocol.h GetDefaultPort(testnet=false)
    public static final int TestPort = 34777;     //protocol.h GetDefaultPort(testnet=true)

    //
    //  Production
    //
    public static final int AddressHeader = 63;             //base58.h CBitcoinAddress::PUBKEY_ADDRESS
    public static final int p2shHeader = 5;             //base58.h CBitcoinAddress::SCRIPT_ADDRESS
    public static final int dumpedPrivateKeyHeader = 128;   //common to all coins
    public static final long PacketMagic = 0x25e4bbf2;

    //Genesis Block Information from main.cpp: LoadBlockIndex
    static public long genesisBlockDifficultyTarget = (0x1e0ffff0L);         //main.cpp: LoadBlockIndex
    static public long genesisBlockTime = 1397300777L;                       
    static public long genesisBlockNonce = (2419556);                        
    static public String genesisHash = "faeaebb8e401dbd2c5af9a82a6594b9fd58f1fbd9f7b6aa950cbd10494192cd6"; //main.cpp: hashGenesisBlock
    static public int genesisBlockValue = 25;                                                              
    //taken from the raw data of the block explorer
    static public String genesisXInBytes = "0002e7034c57417072696c2032303134202d205370656564636f696e205b5350445d20697320616e206f70656e20736f757263652063727970746f2063757272656e637920666f7220776f726c6420696e7465726e6574207573657273";
    static public String genessiXOutBytes = "";

    //net.cpp strDNSSeed
    static public String[] dnsSeeds = new String[] {
		"158.69.52.55",
		"142.4.223.149",
		"51.255.140.174",
		"51.254.223.3"
    };


    //
    // TestNet - speedcoin - not tested / incomplete
    //
    public static final boolean supportsTestNet = false;
    public static final int testnetAddressHeader = 74;             //base58.h CBitcoinAddress::PUBKEY_ADDRESS_TEST
    public static final int testnetp2shHeader = 196;             //base58.h CBitcoinAddress::SCRIPT_ADDRESS_TEST
    public static final long testnetPacketMagic = 0x73706463;
    public static final String testnetGenesisHash = "624854a97f2ecc6bcff214c2f93f4413009f6a738c84d764cd137bda598c46fc";
    static public long testnetGenesisBlockDifficultyTarget = (0x1e0ffff0L);         //main.cpp: LoadBlockIndex
    static public long testnetGenesisBlockTime = 1397300111L;
    static public long testnetGenesisBlockNonce = (413204);





    //main.cpp GetBlockValue(height, fee)
    public static BigInteger GetBlockReward(int height)
    {
            return Utils.toNanoCoins(0, 25).shiftRight(height / subsidyDecreaseBlockCount);
    }
    
 

    public static int subsidyDecreaseBlockCount = 2100000;     //main.cpp GetBlockValue(height, fee)

    public static BigInteger proofOfWorkLimit = Utils.decodeCompactBits(0x1e0fffffL);  //main.cpp bnProofOfWorkLimit (~uint256(0) >> 20); // digitalcoin: starting difficulty is 1 / 2^12

    static public String[] testnetDnsSeeds = new String[] {
          "not supported"
    };
    //from main.h: CAlert::CheckSignature
    public static final String SATOSHI_KEY = "04503b33cd73f442608439c6df4e0a158451d19283dab5a49b3b59e93989c7a15e0e50ed1b81074957a3d60b16678011a36d1bb3ca2ff8007489e084b4e2e901f3";
    public static final String TESTNET_SATOSHI_KEY = "04a8b12a6132226e817ef91bce5621ceda06ec77c8c3cd9ca6d3eb91bfd853d64275594cf1e8e336e46f51b1e2781f11128a245f32ff907b8d41534ed03ef812cd";

    /** The string returned by getId() for the main, production network where people trade things. */
    public static final String ID_MAINNET = "org.speedcoin.production";
    /** The string returned by getId() for the testnet. */
    public static final String ID_TESTNET = "org.speedcoin.test";
    /** Unit test network. */
    public static final String ID_UNITTESTNET = "com.google.speedcoin.unittest";

    //checkpoints.cpp Checkpoints::mapCheckpoints
    public static void initCheckpoints(Map<Integer, Sha256Hash> checkpoints)
    {
	checkpoints.put(0,     new Sha256Hash("faeaebb8e401dbd2c5af9a82a6594b9fd58f1fbd9f7b6aa950cbd10494192cd6")); 
	checkpoints.put(1085,  new Sha256Hash("02549996d5e46822f2ae892abdbdc87c191d932d39bc861f586c7aafaa774965")); 
	checkpoints.put(12000, new Sha256Hash("bfb475d470f769790aa69b46a5edd1b7d30660c7850966b3e327d88fb234cefa")); 
	checkpoints.put(50000, new Sha256Hash("565eadf754bc55654c69cc1d47a4dec9f817821f0e9fd149ce3ac7d9bf742cf1")); 
	checkpoints.put(100000, new Sha256Hash("29269eb1da2ce1e5887ae02a8da31093ddf808e6418dcf27330aaadf64538f80")); 
	checkpoints.put(200000, new Sha256Hash("70f6c983a953f1c1251bd1c30ab33d339c26158cdd8e1990023e4b9ab95c0491")); 
	checkpoints.put(300000, new Sha256Hash("9c11168b00fd3a55f6562c03b4f9c16297eeece2a423b35cf77e15d8d1be2235")); 
	checkpoints.put(400000, new Sha256Hash("533a6265a292487b6bd3bc8a01d3d303dc8b92f73dac3d81cfaea112255b4950")); 
	checkpoints.put(500000, new Sha256Hash("b79c1f3e79e2064b3ef521b59eaf859c93591f1025f7bbd19787599c9e9e0444")); 
	checkpoints.put(600000, new Sha256Hash("5a3ac5d588aabf990ddc5b92238e6a2806cfdf3348bf6a275b8a1789c13be983")); 
	checkpoints.put(700000, new Sha256Hash("17e0a6739cebe7c9cb537ea183fe3bb0513e2926b2644e58a6772492b7c6bef0")); 
	checkpoints.put(750000, new Sha256Hash("67754fd6125f60218fb1a20e7c96647420476e0eb446aeb4c8e23ffcdd4545cf")); 
	checkpoints.put(780000, new Sha256Hash("ae1f2e81e089b9d65ac6f40faf2dd1ac268cc5139adcfd1f388254d43d7020d7")); 
	checkpoints.put(800000, new Sha256Hash("4a1b8ca13d2c2d9769edb3943da198eabb0c400d8d0ddbbde6bac599f0a5b721")); 
	checkpoints.put(801000, new Sha256Hash("726e8d12507fa3bb637b6f174e83bbe0d4239936188fdeb045219004b18835c0")); 
	checkpoints.put(802000, new Sha256Hash("7d926f23baa1cb7bb75c284e9adbc35af17d2ae600e22bb6de33e78919ed8eb9")); 
    }


}
