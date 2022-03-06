package com.jnicram.notificationsgrpcclient;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ClientHelper {

  public static ManagedChannel getGrpcChannel() {
   return ManagedChannelBuilder
        .forAddress("localhost", 9090)
        .usePlaintext()
        .build();
  }
}
