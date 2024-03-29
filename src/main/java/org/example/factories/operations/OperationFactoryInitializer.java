package org.example.factories.operations;

import org.example.exceptions.HolderException;
import org.example.hw2.operations.*;
import org.example.hw2.storages.GroupedGoodStorage;
import org.example.hw5.operations.CreateGoodOperation;
import org.example.hw5.operations.DeleteGoodOperation;
import org.example.hw5.operations.GetGoodOperation;
import org.example.hw5.operations.UpdateGoodOperation;

import java.util.function.Function;

public class OperationFactoryInitializer {
    public static void holdAllOperations() throws HolderException {
        for(var operationType : Operations.values()) {
            if(OperationFactory.holds(operationType)) continue;
            var constructor = getConstructor(operationType);
            OperationFactory.hold(operationType, constructor);
        }
    }

    public static void releaseAllOperations() throws HolderException {
        for(var operationType : Operations.values()) {
            if(OperationFactory.holds(operationType))
                OperationFactory.release(operationType);
        }
    }

    private static Function<GroupedGoodStorage, Operation> getConstructor(Operations type) {
        return switch (type) {
            case GET_GOOD_QUANTITY -> GetGoodQuantityOperation::new;
            case SUBTRACT_GOOD_QUANTITY -> SubtractGoodQuantityOperation::new;
            case ADD_GOOD_QUANTITY -> AddGoodQuantityOperation::new;
            case ADD_GOOD_GROUP -> AddGoodGroupOperation::new;
            case ADD_GOOD_NAME_TO_GROUP -> AddGoodNameToGroup::new;
            case SET_GOOD_PRICE -> SetGoodPriceOperation::new;

            case GET_GOOD -> GetGoodOperation::new;
            case CREATE_GOOD -> CreateGoodOperation::new;
            case UPDATE_GOOD -> UpdateGoodOperation::new;
            case DELETE_GOOD -> DeleteGoodOperation::new;
        };
    }
}
