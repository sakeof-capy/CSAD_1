package org.example.hw2.basis;

import org.example.packets.data.Packet;

public interface Processor extends AutoCloseable {
    void process(Packet packet);
}
