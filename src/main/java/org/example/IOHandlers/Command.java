package org.example.IOHandlers;

public record Command(String type,
                      String id,
                      String startRoad,
                      String endRoad) {

    Command(String type){
        this(type,null,null,null);
    }
    Command(String type,String id,
            String onDirection){
        this(type,id,onDirection,onDirection);
    }
}
