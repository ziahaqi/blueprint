package com.ziahaqi.printlibs.factory;

import com.ziahaqi.printlibs.builder.DriverBuilder;

/**
 * Created by zinux on 13/07/15.
 *
 */
public class PrinterFactory {

    public static DriverBuilder init(DriverBuilder builder){
        return builder;
    }

}
