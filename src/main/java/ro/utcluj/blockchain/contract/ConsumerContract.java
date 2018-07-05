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
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.StaticArray32;
import org.web3j.abi.datatypes.generated.StaticArray8;
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
public class ConsumerContract extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b50604051610909380380610909833981016040908152815160208301519183015160608085015160008054600160a060020a03338116600160a060020a031992831617909255600180543290931692909116919091179055600a819055928501949384019391820192916080019061008c90600b9083906100d3565b5084516100a090606b906020880190610111565b5083516100b490606c906020870190610111565b5082516100c890606d906020860190610111565b50505050505061019b565b8260608101928215610101579160200282015b828111156101015782518255916020019190600101906100e6565b5061010d92915061017e565b5090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061015257805160ff1916838001178555610101565b8280016001018555821561010157918201828111156101015782518255916020019190600101906100e6565b61019891905b8082111561010d5760008155600101610184565b90565b61075f806101aa6000396000f30060806040526004361061008d5763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166312065fe081146100925780631e979f23146100b95780633297fc00146100d65780638caa872e14610124578063967020be14610169578063c12cfa631461017e578063c2614831146101bc578063d73e8d42146101d1575b600080fd5b34801561009e57600080fd5b506100a76101fc565b60408051918252519081900360200190f35b3480156100c557600080fd5b506100d46004356024356102a6565b005b3480156100e257600080fd5b506100eb61053d565b604051808261040080838360005b838110156101115781810151838201526020016100f9565b5050505090500191505060405180910390f35b34801561013057600080fd5b50604080516101008181019092526100d49136916004916101049190839060089083908390808284375093965061058a95505050505050565b34801561017557600080fd5b506100eb61059b565b34801561018a57600080fd5b506101936105e2565b6040805173ffffffffffffffffffffffffffffffffffffffff9092168252519081900360200190f35b3480156101c857600080fd5b506100eb6105fe565b3480156101dd57600080fd5b506101e6610642565b60405181518152808261010080838360206100f9565b60008054600154604080517f4773489200000000000000000000000000000000000000000000000000000000815273ffffffffffffffffffffffffffffffffffffffff928316600482015290519190921691634773489291602480830192602092919082900301818787803b15801561027457600080fd5b505af1158015610288573d6000803e3d6000fd5b505050506040513d602081101561029e57600080fd5b505190505b90565b60078116600080808080600286600881106102bd57fe5b015494506102db85896064028115156102d257fe5b0460640361067e565b600a5490945060019350841161033b5760408051878152602081018790528082018a90529051600260ff86168b020493507f9a81468e96422914fe2dffa77b0ee96f4e6b99652f6d910ee02fbeaba97a32da9181900360600190a161041a565b505060008054604080517f7324bbcf0000000000000000000000000000000000000000000000000000000081526004810189905289870360248201819052915160ff86168b0260020294929373ffffffffffffffffffffffffffffffffffffffff1692637324bbcf926044808201939182900301818387803b1580156103c057600080fd5b505af11580156103d4573d6000803e3d6000fd5b505060408051898152602081018990528082018c905290517f0b5cffbf550b354b6e18c044f10597ef01d1ffddbb536fffdf7d94012cfc1bcb9350908190036060019150a15b60008054604080517f8e3d489e000000000000000000000000000000000000000000000000000000008152600481018b9052905173ffffffffffffffffffffffffffffffffffffffff90921692638e3d489e9260248084019382900301818387803b15801561048857600080fd5b505af115801561049c573d6000803e3d6000fd5b505060008054600154604080517fcf34cde600000000000000000000000000000000000000000000000000000000815273ffffffffffffffffffffffffffffffffffffffff928316600482015260248101899052905191909216945063cf34cde693506044808301939282900301818387803b15801561051b57600080fd5b505af115801561052f573d6000803e3d6000fd5b505050505050505050505050565b61054561069b565b61054d61069b565b60005b602081101561058457600b604082016060811061056957fe5b015482826020811061057757fe5b6020020152600101610550565b50919050565b61059760028260086106bb565b5050565b6105a361069b565b6105ab61069b565b60005b602081101561058457600b60208201606081106105c757fe5b01548282602081106105d557fe5b60200201526001016105ae565b60015473ffffffffffffffffffffffffffffffffffffffff1690565b61060661069b565b61060e61069b565b60005b602081101561058457600b816060811061062757fe5b015482826020811061063557fe5b6020020152600101610611565b61064a6106f9565b604080516101008101918290529060029060089082845b815481526020019060010190808311610661575050505050905090565b60008082101561069357816000039050610696565b50805b919050565b610400604051908101604052806020906020820280388339509192915050565b82600881019282156106e9579160200282015b828111156106e95782518255916020019190600101906106ce565b506106f5929150610719565b5090565b610100604051908101604052806008906020820280388339509192915050565b6102a391905b808211156106f5576000815560010161071f5600a165627a7a7230582052572e597bd402abc3a8349c5b27bb0699a70c71a65a81331d1b753e556cfaed0029";

    protected ConsumerContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ConsumerContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<FineConsumerEventResponse> getFineConsumerEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("FineConsumer", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<FineConsumerEventResponse> responses = new ArrayList<FineConsumerEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            FineConsumerEventResponse typedResponse = new FineConsumerEventResponse();
            typedResponse.amountPaid = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.totalDeviation = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<FineConsumerEventResponse> fineConsumerEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("FineConsumer", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, FineConsumerEventResponse>() {
            @Override
            public FineConsumerEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                FineConsumerEventResponse typedResponse = new FineConsumerEventResponse();
                typedResponse.amountPaid = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.totalDeviation = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public List<RewardConsumerEventResponse> getRewardConsumerEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("RewardConsumer", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<RewardConsumerEventResponse> responses = new ArrayList<RewardConsumerEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            RewardConsumerEventResponse typedResponse = new RewardConsumerEventResponse();
            typedResponse.amountPaid = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<RewardConsumerEventResponse> rewardConsumerEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("RewardConsumer", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, RewardConsumerEventResponse>() {
            @Override
            public RewardConsumerEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                RewardConsumerEventResponse typedResponse = new RewardConsumerEventResponse();
                typedResponse.amountPaid = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
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

    public RemoteCall<BigInteger> getBalance() {
        Function function = new Function("getBalance", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> checkValue(BigInteger _realValue, BigInteger _generalIndex) {
        Function function = new Function(
                "checkValue", 
                Arrays.<Type>asList(new Uint256(_realValue),
                new Uint256(_generalIndex)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<List> getThirdBaselineConsumptionCurve() {
        Function function = new Function("getThirdBaselineConsumptionCurve", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<StaticArray32<Uint256>>() {}));
        return executeRemoteCallSingleValueReturn(function, List.class);
    }

    public RemoteCall<TransactionReceipt> setCurve(List<BigInteger> _curve) {
        Function function = new Function(
                "setCurve", 
                Arrays.<Type>asList(new StaticArray8<Uint256>(
                        org.web3j.abi.Utils.typeMap(_curve, Uint256.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<List> getSecondBaselineConsumptionCurve() {
        Function function = new Function("getSecondBaselineConsumptionCurve", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<StaticArray32<Uint256>>() {}));
        return executeRemoteCallSingleValueReturn(function, List.class);
    }

    public RemoteCall<String> getOwnerAdress() {
        Function function = new Function("getOwnerAdress", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<List> getFirstBaselineConsumptionCurve() {
        Function function = new Function("getFirstBaselineConsumptionCurve", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<StaticArray32<Uint256>>() {}));
        return executeRemoteCallSingleValueReturn(function, List.class);
    }

    public RemoteCall<List> getCurve() {
        Function function = new Function("getCurve", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<StaticArray8<Uint256>>() {}));
        return executeRemoteCallSingleValueReturn(function, List.class);
    }

    public static RemoteCall<ConsumerContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _name, String _homeAddress, String _CNP, BigInteger _deviation, List<BigInteger> _expectedConsumption) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_name), 
                new org.web3j.abi.datatypes.Utf8String(_homeAddress), 
                new org.web3j.abi.datatypes.Utf8String(_CNP), 
                new Uint256(_deviation),
                new org.web3j.abi.datatypes.StaticArray<Uint256>(
                        org.web3j.abi.Utils.typeMap(_expectedConsumption, Uint256.class))));
        return deployRemoteCall(ConsumerContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<ConsumerContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _name, String _homeAddress, String _CNP, BigInteger _deviation, List<BigInteger> _expectedConsumption) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_name), 
                new org.web3j.abi.datatypes.Utf8String(_homeAddress), 
                new org.web3j.abi.datatypes.Utf8String(_CNP), 
                new Uint256(_deviation),
                new org.web3j.abi.datatypes.StaticArray<Uint256>(
                        org.web3j.abi.Utils.typeMap(_expectedConsumption, Uint256.class))));
        return deployRemoteCall(ConsumerContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static ConsumerContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ConsumerContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static ConsumerContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ConsumerContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class FineConsumerEventResponse {
        public BigInteger amountPaid;

        public BigInteger totalDeviation;
    }

    public static class RewardConsumerEventResponse {
        public BigInteger amountPaid;
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
