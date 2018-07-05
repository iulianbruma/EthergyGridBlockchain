package ro.utcluj.blockchain.contract;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.StaticArray32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.2.0.
 */
public class ProducerContract extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b506040516106fe3803806106fe833981016040908152815160208301519183015160008054600160a060020a03338116600160a060020a0319928316179092556001805432909316929091169190911790559083019291820191908101906060908101906100829060029083906100c8565b508351610096906062906020870190610106565b5082516100aa906063906020860190610106565b5081516100be906064906020850190610106565b5050505050610190565b82606081019282156100f6579160200282015b828111156100f65782518255916020019190600101906100db565b50610102929150610173565b5090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061014757805160ff19168380011785556100f6565b828001600101855582156100f657918201828111156100f65782518255916020019190600101906100db565b61018d91905b808211156101025760008155600101610179565b90565b61055f8061019f6000396000f30060806040526004361061006c5763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166311d62e71811461007157806312065fe0146100bf578063465585e2146100e6578063bb9ef60114610103578063fcd9c2e214610118575b600080fd5b34801561007d57600080fd5b5061008661012d565b604051808261040080838360005b838110156100ac578181015183820152602001610094565b5050505090500191505060405180910390f35b3480156100cb57600080fd5b506100d461017a565b60408051918252519081900360200190f35b3480156100f257600080fd5b50610101600435602435610223565b005b34801561010f57600080fd5b50610086610488565b34801561012457600080fd5b506100866104cc565b610135610513565b61013d610513565b60005b6020811015610174576002604082016060811061015957fe5b015482826020811061016757fe5b6020020152600101610140565b50919050565b60008054600154604080517f4773489200000000000000000000000000000000000000000000000000000000815273ffffffffffffffffffffffffffffffffffffffff928316600482015290519190921691634773489291602480830192602092919082900301818787803b1580156101f257600080fd5b505af1158015610206573d6000803e3d6000fd5b505050506040513d602081101561021c57600080fd5b5051905090565b6001826000806002856060811061023657fe5b0154915050808503600381138061024e575060021981125b156103265760008054604080517f7324bbcf0000000000000000000000000000000000000000000000000000000081526004810189905260248101859052905173ffffffffffffffffffffffffffffffffffffffff90921692637324bbcf9260448084019382900301818387803b1580156102c857600080fd5b505af11580156102dc573d6000803e3d6000fd5b505060408051888152602081018690528082018a905290517f0b5cffbf550b354b6e18c044f10597ef01d1ffddbb536fffdf7d94012cfc1bcb9350908190036060019150a1610367565b604080518681526020810184905280820188905290517f9a81468e96422914fe2dffa77b0ee96f4e6b99652f6d910ee02fbeaba97a32da9181900360600190a15b60008054604080517f8e3d489e00000000000000000000000000000000000000000000000000000000815260048101899052905173ffffffffffffffffffffffffffffffffffffffff90921692638e3d489e9260248084019382900301818387803b1580156103d557600080fd5b505af11580156103e9573d6000803e3d6000fd5b505060008054600154604080517f193f974c00000000000000000000000000000000000000000000000000000000815273ffffffffffffffffffffffffffffffffffffffff9283166004820152602481018a9052905191909216945063193f974c93506044808301939282900301818387803b15801561046857600080fd5b505af115801561047c573d6000803e3d6000fd5b50505050505050505050565b610490610513565b610498610513565b60005b602081101561017457600281606081106104b157fe5b01548282602081106104bf57fe5b602002015260010161049b565b6104d4610513565b6104dc610513565b60005b602081101561017457600260208201606081106104f857fe5b015482826020811061050657fe5b60200201526001016104df565b6104006040519081016040528060209060208202803883395091929150505600a165627a7a72305820f2a4a75bad8b39ab425f694182f1cf2c04e22dee77084ca60739262079ba99ca0029";

    protected ProducerContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ProducerContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<OkValueEventResponse> getOkValueEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("OkValue", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<OkValueEventResponse> responses = new ArrayList<OkValueEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            OkValueEventResponse typedResponse = new OkValueEventResponse();
            typedResponse._index = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.requiredValue = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.realValue = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<OkValueEventResponse> okValueEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("OkValue", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, OkValueEventResponse>() {
            @Override
            public OkValueEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                OkValueEventResponse typedResponse = new OkValueEventResponse();
                typedResponse._index = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.requiredValue = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.realValue = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public List<DeviationOccuredEventResponse> getDeviationOccuredEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("DeviationOccured", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<DeviationOccuredEventResponse> responses = new ArrayList<DeviationOccuredEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            DeviationOccuredEventResponse typedResponse = new DeviationOccuredEventResponse();
            typedResponse._index = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.requiredValue = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.realValue = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<DeviationOccuredEventResponse> deviationOccuredEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("DeviationOccured", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, DeviationOccuredEventResponse>() {
            @Override
            public DeviationOccuredEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                DeviationOccuredEventResponse typedResponse = new DeviationOccuredEventResponse();
                typedResponse._index = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.requiredValue = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.realValue = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<List> getThirdBaselineProductionCurve() {
        Function function = new Function("getThirdBaselineProductionCurve", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<StaticArray32<Uint256>>() {}));
        return executeRemoteCallSingleValueReturn(function, List.class);
    }

    public RemoteCall<BigInteger> getBalance() {
        Function function = new Function("getBalance", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> registerProduction(BigInteger _value, BigInteger _generalIndex) {
        Function function = new Function(
                "registerProduction", 
                Arrays.<Type>asList(new Uint256(_value),
                new Uint256(_generalIndex)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<List> getFirstBaselineProductionCurve() {
        Function function = new Function("getFirstBaselineProductionCurve", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<StaticArray32<Uint256>>() {}));
        return executeRemoteCallSingleValueReturn(function, List.class);
    }

    public RemoteCall<List> getSecondBaselineProductionCurve() {
        Function function = new Function("getSecondBaselineProductionCurve", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<StaticArray32<Uint256>>() {}));
        return executeRemoteCallSingleValueReturn(function, List.class);
    }

    public static RemoteCall<ProducerContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _name, String _homeAddress, String _CNP, List<BigInteger> _expectedProduction) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_name), 
                new org.web3j.abi.datatypes.Utf8String(_homeAddress), 
                new org.web3j.abi.datatypes.Utf8String(_CNP), 
                new org.web3j.abi.datatypes.StaticArray<Uint256>(
                        org.web3j.abi.Utils.typeMap(_expectedProduction, Uint256.class))));
        return deployRemoteCall(ProducerContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<ProducerContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _name, String _homeAddress, String _CNP, List<BigInteger> _expectedProduction) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_name), 
                new org.web3j.abi.datatypes.Utf8String(_homeAddress), 
                new org.web3j.abi.datatypes.Utf8String(_CNP), 
                new org.web3j.abi.datatypes.StaticArray<Uint256>(
                        org.web3j.abi.Utils.typeMap(_expectedProduction, Uint256.class))));
        return deployRemoteCall(ProducerContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static ProducerContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ProducerContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static ProducerContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ProducerContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class OkValueEventResponse {
        public BigInteger _index;

        public BigInteger requiredValue;

        public BigInteger realValue;
    }

    public static class DeviationOccuredEventResponse {
        public BigInteger _index;

        public BigInteger requiredValue;

        public BigInteger realValue;
    }
}
