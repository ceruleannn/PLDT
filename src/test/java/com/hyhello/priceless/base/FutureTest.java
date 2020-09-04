package com.hyhello.priceless.base;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 *
 */
public class FutureTest {

    public static void main(String[] args) {

         CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
                System.out.println("12");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return  "";
        }).thenApplyAsync(s-> {return 1;})
            .thenApplyAsync(a-> {
                     System.out.println(a+1);
                     return a + 1;
           });
    }
}
