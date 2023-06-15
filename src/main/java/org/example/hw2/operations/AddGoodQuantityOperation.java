package org.example.hw2.operations;

import org.example.exceptions.StorageException;
import org.example.hw2.goods.StandardGood;
import org.example.hw2.storages.GroupedGoodStorage;

import java.util.Optional;

public class AddGoodQuantityOperation implements Operation {
    public AddGoodQuantityOperation(GroupedGoodStorage storage) {
        this.storage = storage;
        this.result = null;
    }

    @Override
    public void execute(OperationParams params) throws StorageException {
        var goodName = params.getGoodName();
        var quantity = params.getQuantity();
        var prevGood = storage.getGood(goodName)
                .orElseThrow(() -> new StorageException("Good " + goodName + " does not exist."));
        var prevQuantity = prevGood.getQuantity();
        var prevPrice = prevGood.getPrice();
        storage.updateGood(new StandardGood(goodName, prevQuantity + quantity, prevPrice));
        System.out.println("Quantity updated: " + storage.getGood(goodName).get().getQuantity());
    }

    @Override
    public Optional<Integer> getResult() {
        return Optional.ofNullable(result);
    }

    private Integer result;
    private final GroupedGoodStorage storage;
}
