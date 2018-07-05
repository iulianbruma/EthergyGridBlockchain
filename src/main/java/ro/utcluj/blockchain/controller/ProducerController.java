package ro.utcluj.blockchain.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.utcluj.blockchain.model.Consumer;
import ro.utcluj.blockchain.model.Producer;
import ro.utcluj.blockchain.model.request.ValueRequest;
import ro.utcluj.blockchain.model.response.ValueResponse;
import ro.utcluj.blockchain.service.ProducerService;

import java.util.List;

@RestController
@CrossOrigin(maxAge = 84600)
@RequestMapping("/producer")
public class ProducerController {

    @Autowired
    private ProducerService producerService;

    @GetMapping
    @ApiOperation(value = "Get all producers", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all producers"),
            @ApiResponse(code = 404, message = "No producers were found"),
            @ApiResponse(code = 500, message = "Internal Server Error!")
    })
    public ResponseEntity<List<Producer>> getAllProducer() {
        return new ResponseEntity<>(producerService.getAllProducer(), HttpStatus.OK);
    }

    @GetMapping("/{cnp}")
    @ApiOperation(value = "Get the producer", response = Producer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the producer"),
            @ApiResponse(code = 404, message = "The producer was not found for this CNP"),
            @ApiResponse(code = 500, message = "Internal Server Error!")
    })
    public ResponseEntity<Producer> getProducer(@PathVariable("cnp") String CNP) {
        return new ResponseEntity<>(producerService.getProducer(CNP), HttpStatus.OK);
    }

    @PostMapping("/value")
    @ApiOperation(value = "Check production for a producer", response = ValueResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully checked the production value"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The producer contract was not deployed for this CNP"),
            @ApiResponse(code = 500, message = "Internal Server Error!")
    })
    public ResponseEntity<ValueResponse> registerProduction(@RequestBody ValueRequest valueRequest) {
        return new ResponseEntity<>(producerService.registerProduction(valueRequest), HttpStatus.OK);
    }

    @GetMapping("/{cnp}/baselineConsumption")
    @ApiOperation(value = "Get baseline consumption curve for a producer", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the baseline consumption curve"),
            @ApiResponse(code = 500, message = "Internal Server Error!")
    })
    public ResponseEntity<List<Integer>> getBaselineConsumptionCurve(@PathVariable("cnp") String CNP) {
        return new ResponseEntity<>(producerService.getBaselineConsumptionCurve(CNP), HttpStatus.OK);
    }

    @GetMapping("/{cnp}/balance")
    @ApiOperation(value = "Get balance for a producer", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the balance for producer"),
            @ApiResponse(code = 500, message = "Internal Server Error!")
    })
    public ResponseEntity<Integer> getBalance(@PathVariable("cnp") String CNP) {
        return new ResponseEntity<>(producerService.getBalance(CNP), HttpStatus.OK);
    }
}
