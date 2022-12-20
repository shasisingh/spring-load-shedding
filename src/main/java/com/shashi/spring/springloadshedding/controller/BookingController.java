package com.shashi.spring.springloadshedding.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "/api/v1/booking")
public class BookingController {

  @GetMapping("book")
  public String greeting(){
    await(10000);
    return "Hello! You are welcome here!";
  }

  /**
   * Simulating real situation where service is going to take some time.
   */
  private void await(long timeToWait){
    try {
      Thread.sleep(timeToWait);
    } catch (InterruptedException e) {
    }
  }
}
