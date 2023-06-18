package org.example.hw2.operations;

import org.example.exceptions.StorageException;
import org.example.hw2.goods.StandardGood;
import org.example.hw2.storages.GroupedGoodStorage;

import java.util.Optional;

public class AddGoodNameToGroup implements Operation {
    public AddGoodNameToGroup(GroupedGoodStorage storage) {
        this.storage = storage;
        this.result = null;
    }

    @Override
    public void execute(OperationParams params) throws StorageException {
        var groupName = params.getGroupName();
        var goodName = params.getGoodName();
        var goodPrice = params.getPrice();
        var good = new StandardGood(goodName, goodPrice);
        storage.addGoodToGroup(good, groupName);
    }

    @Override
    public Optional<Integer> getResult() {
        return Optional.ofNullable(result);
    }

    private Integer result;
    private final GroupedGoodStorage storage;
}
