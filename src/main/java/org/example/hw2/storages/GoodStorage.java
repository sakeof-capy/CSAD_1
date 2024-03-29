package org.example.hw2.storages;

import org.example.exceptions.storage.StorageException;
import org.example.hw2.goods.Good;
import org.example.hw4.criteria.Criterion;

import java.util.Optional;

public interface GoodStorage {
    void addGoodToGroup(Good good, String groupName) throws StorageException;
    Optional<Good> getGood(String goodName);
    void updateGood(Good good) throws StorageException;
    void deleteGood(String name) throws StorageException;
    Iterable<Good> getGoodsListByCriterion(Criterion criterion) throws StorageException;
}
