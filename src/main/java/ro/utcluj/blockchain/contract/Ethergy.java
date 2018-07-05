package ro.utcluj.blockchain.contract;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.2.0.
 */
public class Ethergy extends Contract {
    private static final String BINARY = "60806040526002805460ff1916601217905534801561001d57600080fd5b5060405161050b38038061050b8339810160409081528151602080840151838501516002805433600160a060020a031660008181526003875297882060ff909216600a0a8702909155815461010060a860020a031916610100909102179055908501805193959094910192610094928501906100b1565b5080516100a89060019060208401906100b1565b5050505061014c565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100f257805160ff191683800117855561011f565b8280016001018555821561011f579182015b8281111561011f578251825591602001919060010190610104565b5061012b92915061012f565b5090565b61014991905b8082111561012b5760008155600101610135565b90565b6103b08061015b6000396000f3006080604052600436106100825763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166306fdde038114610087578063313ce56714610111578063477348921461013c57806370a082311461016f57806395d89b4114610190578063f3fef3a3146101a5578063f5d82b6b146101cb575b600080fd5b34801561009357600080fd5b5061009c6101ef565b6040805160208082528351818301528351919283929083019185019080838360005b838110156100d65781810151838201526020016100be565b50505050905090810190601f1680156101035780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561011d57600080fd5b5061012661027d565b6040805160ff9092168252519081900360200190f35b34801561014857600080fd5b5061015d600160a060020a0360043516610286565b60408051918252519081900360200190f35b34801561017b57600080fd5b5061015d600160a060020a03600435166102a1565b34801561019c57600080fd5b5061009c6102b3565b3480156101b157600080fd5b506101c9600160a060020a036004351660243561030d565b005b3480156101d757600080fd5b506101c9600160a060020a0360043516602435610348565b6000805460408051602060026001851615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156102755780601f1061024a57610100808354040283529160200191610275565b820191906000526020600020905b81548152906001019060200180831161025857829003601f168201915b505050505081565b60025460ff1681565b600160a060020a031660009081526003602052604090205490565b60036020526000908152604090205481565b60018054604080516020600284861615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156102755780601f1061024a57610100808354040283529160200191610275565b600160a060020a0391821660009081526003602052604080822080548490039055600254610100900490931681529190912080549091019055565b600254600160a060020a0361010090910481166000908152600360205260408082208054859003905593909116815291909120805490910190555600a165627a7a72305820b0da9a31d726d36058a030f84c5dc64db5a2e69163b51a57d98b16edf0b7d7300029";

    protected Ethergy(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Ethergy(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<String> name() {
        Function function = new Function("name", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> decimals() {
        Function function = new Function("decimals", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getUserBalance(String _userAddress) {
        Function function = new Function("getUserBalance", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_userAddress)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> balanceOf(String param0) {
        Function function = new Function("balanceOf", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> symbol() {
        Function function = new Function("symbol", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> withdraw(String _userAddress, BigInteger _amount) {
        Function function = new Function(
                "withdraw", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_userAddress), 
                new Uint256(_amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> add(String _userAddress, BigInteger _amount) {
        Function function = new Function(
                "add", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_userAddress), 
                new Uint256(_amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<Ethergy> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger _initialSupply, String _tokenName, String _tokenSymbol) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Uint256(_initialSupply),
                new Utf8String(_tokenName),
                new Utf8String(_tokenSymbol)));
        return deployRemoteCall(Ethergy.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<Ethergy> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _initialSupply, String _tokenName, String _tokenSymbol) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Uint256(_initialSupply),
                new Utf8String(_tokenName),
                new Utf8String(_tokenSymbol)));
        return deployRemoteCall(Ethergy.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static Ethergy load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Ethergy(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Ethergy load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Ethergy(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
