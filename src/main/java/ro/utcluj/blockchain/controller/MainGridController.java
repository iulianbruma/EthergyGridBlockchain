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
import ro.utcluj.blockchain.model.request.TokenRequest;
import ro.utcluj.blockchain.model.request.UserRequest;
import ro.utcluj.blockchain.model.response.Grid;
import ro.utcluj.blockchain.model.response.ValueResponse;
import ro.utcluj.blockchain.service.GridService;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;

@RestController
@CrossOrigin(maxAge = 84600)
@RequestMapping("/grid")
public class MainGridController {

    @Autowired
    private GridService gridService;

    @GetMapping
    @ApiOperation(value = "Deploy the GRID contract into the blockchain network", response = Grid.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deployed contract"),
            @ApiResponse(code = 409, message = "The main grid is already deployed"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<Grid> deployGridContract() {
        return new ResponseEntity<>(gridService.deployContract(), HttpStatus.OK);
    }

    @PostMapping("/consumer")
    @ApiOperation(value = "Register new consumer", response = Consumer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully registered a new consumer"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<Consumer> registerConsumer(@Valid @RequestBody UserRequest user) {
        return new ResponseEntity<>(gridService.registerConsumer(user), HttpStatus.OK);
    }

    @PostMapping("/producer")
    @ApiOperation(value = "Register new producer", response = Producer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully registered a new producer"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<Producer> registerProducer(@Valid @RequestBody UserRequest userRequest) {
        return new ResponseEntity<>(gridService.registerProducer(userRequest), HttpStatus.OK);
    }

    @PostMapping("/token")
    @ApiOperation(value = "Allocate token for user", response = Integer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully checked the value"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The consumer contract was not deployed for this CNP"),
            @ApiResponse(code = 500, message = "Internal Server Error!")
    })
    public ResponseEntity<Integer> checkValue(@RequestBody TokenRequest tokenRequest) {
        return new ResponseEntity<>(gridService.addToken(tokenRequest), HttpStatus.OK);
    }

    @GetMapping("/balance/{cnp}")
    @ApiOperation(value = "Get balance for user", response = Integer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the balance"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<Integer> getBalance(@PathVariable("cnp") String CNP) {
        return new ResponseEntity<>(gridService.getBalance(CNP), HttpStatus.OK);
    }

    @GetMapping("/price")
    @ApiOperation(value = "Get price per kWh", response = Integer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved price"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<Integer> getPricePerkWh() {
        return new ResponseEntity<>(gridService.getPricePerkWh(), HttpStatus.OK);
    }

    @GetMapping("/balanceCurve")
    @ApiOperation(value = "Get balance curve", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get the balance curve"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<List<Integer>> getBalanceCurve() {
        return new ResponseEntity<>(gridService.getBalanceCurve(), HttpStatus.OK);
    }

    @GetMapping("/demandCurve")
    @ApiOperation(value = "Get demand curve", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get the demand curve"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<List<Integer>> getDemandCurve() {
        return new ResponseEntity<>(gridService.getDemandCurve(), HttpStatus.OK);
    }

    @GetMapping("/productionCurve")
    @ApiOperation(value = "Get production curve", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get the production curve"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<List<Integer>> getProductionCurve() {
        return new ResponseEntity<>(gridService.getProductionCurve(), HttpStatus.OK);
    }

    @GetMapping("/simulation")
    @ApiOperation(value = "Start simulation", response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully started the simulation"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<Void> startSimulation() {
        gridService.startSimulation();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
