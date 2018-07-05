package ro.utcluj.blockchain.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.utcluj.blockchain.model.Consumer;
import ro.utcluj.blockchain.model.request.TokenRequest;
import ro.utcluj.blockchain.model.request.ValueRequest;
import ro.utcluj.blockchain.model.response.ValueResponse;
import ro.utcluj.blockchain.service.ConsumerService;

import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@CrossOrigin(maxAge = 84600)
@RequestMapping("/consumer")
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

    @GetMapping
    @ApiOperation(value = "Get all consumers", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all consumers"),
            @ApiResponse(code = 404, message = "No consumers were found"),
            @ApiResponse(code = 500, message = "Internal Server Error!")
    })
    public ResponseEntity<List<Consumer>> getAllConsumer() {
        System.out.println("Intrat");
        return new ResponseEntity<>(consumerService.getAllConsumer(), HttpStatus.OK);
    }

    @GetMapping("/{cnp}")
    @ApiOperation(value = "Get the consumer", response = Consumer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the consumer"),
            @ApiResponse(code = 404, message = "The consumer was not found for this CNP"),
            @ApiResponse(code = 500, message = "Internal Server Error!")
    })
    public ResponseEntity<Consumer> getConsumer(@PathVariable("cnp") String CNP) {
        return new ResponseEntity<>(consumerService.getConsumer(CNP), HttpStatus.OK);
    }

    @PostMapping("/value")
    @ApiOperation(value = "Check Value for a consumer", response = ValueResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully checked the value"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The consumer contract was not deployed for this CNP"),
            @ApiResponse(code = 500, message = "Internal Server Error!")
    })
    public ResponseEntity<ValueResponse> checkValue(@RequestBody ValueRequest valueRequest) {
        return new ResponseEntity<>(consumerService.checkValue(valueRequest), HttpStatus.OK);
    }

    @GetMapping("/{cnp}/curve")
    @ApiOperation(value = "Get curve for a consumer", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the curve"),
            @ApiResponse(code = 500, message = "Internal Server Error!")
    })
    public ResponseEntity<List<Integer>> getCurve(@PathVariable("cnp") String CNP) {
        return new ResponseEntity<>(consumerService.getCurve(CNP), HttpStatus.OK);
    }

    @GetMapping("/{cnp}/baselineConsumption")
    @ApiOperation(value = "Get baseline consumption curve for a consumer", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the baseline consumption curve"),
            @ApiResponse(code = 500, message = "Internal Server Error!")
    })
    public ResponseEntity<List<Integer>> getBaselineConsumptionCurve(@PathVariable("cnp") String CNP) {
        return new ResponseEntity<>(consumerService.getBaselineConsumptionCurve(CNP), HttpStatus.OK);
    }

    @GetMapping("/{cnp}/balance")
    @ApiOperation(value = "Get balance for a consumer", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the balance for consumer"),
            @ApiResponse(code = 500, message = "Internal Server Error!")
    })
    public ResponseEntity<Integer> getBalance(@PathVariable("cnp") String CNP) {
        return new ResponseEntity<>(consumerService.getBalance(CNP), HttpStatus.OK);
    }

}
