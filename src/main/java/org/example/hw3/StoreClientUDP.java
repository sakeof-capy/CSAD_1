package org.example.hw3;

import org.example.exceptions.ClientException;
import org.example.exceptions.CodecException;
import org.example.exceptions.CreationException;
import org.example.factories.codec.PacketCodecFactory;
import org.example.factories.hw2.PacketFactory;
import org.example.factories.interfaces.DoubleParamFactory;
import org.example.hw2.operations.OperationParams;
import org.example.hw2.operations.Operations;
import org.example.packets.data.Message;
import org.example.packets.data.Packet;
import org.example.packets.encoding.Codec;
import org.example.utilities.ServerUtils;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class StoreClientUDP implements Client {
    public StoreClientUDP(Codec<Packet> codec, DoubleParamFactory<Packet, Byte, Message> packetFactory) {
        this.codec = codec;
        this.packetFactory = packetFactory;
    }
    @Override
    public Message sendMessage(InetAddress serverAddress, int serverPort, Operations operationType, OperationParams params) throws ClientException {
        connect(serverAddress, serverPort);
        Packet packet;
        try {
            packet = packetFactory.create((byte) 2, new Message(operationType,
                    userId.getAndIncrement(), params.toString()));
            var encrypted = codec.encrypt(packet);
            var datagramPacket = new DatagramPacket(encrypted, encrypted.length, serverAddress, serverPort);
            socket.send(datagramPacket);

            var responseBuffer = new byte[ServerUtils.MAX_PACKET_SIZE];
            var responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
            socket.receive(responsePacket);
            var received = Arrays.copyOfRange(responsePacket.getData(), 0, responsePacket.getLength());
            var decrypted = codec.decode(received);
            return decrypted.message();
        } catch (CreationException e) {
            throw new RuntimeException(e);
        } catch (CodecException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            disconnect();
        }
    }

    private void connect(InetAddress serverAddress, int serverPort) throws ClientException {
        try {
            this.socket = new DatagramSocket();
            this.serverAdress = serverAddress;
            this.serverPort = serverPort;
        } catch (SocketException e) {
            throw new ClientException(e.getMessage());
        }
    }

    private void disconnect() {
        if (socket != null) {
            socket.close();
            socket = null;
            serverAdress = null;
            serverPort = -1;
        }
    }

    public static void main(String[] args) {
        try {
            var serverAddress = InetAddress.getLocalHost();
            var codecFactory = new PacketCodecFactory();
            var codec = codecFactory.create();
            var packetFactory = new PacketFactory();
            try {
                var client = new StoreClientUDP(codec, packetFactory);
                var received = client.sendMessage(serverAddress, ServerUtils.PORT, Operations.GET_GOOD_QUANTITY,
                        new OperationParams("", "Milk", 0, 0));
                System.out.println(received);
                received = client.sendMessage(serverAddress, ServerUtils.PORT, Operations.GET_GOOD_QUANTITY,
                        new OperationParams("", "Milk", 0, 0));
                System.out.println(received);
            } catch (ClientException e) {
                throw new RuntimeException(e);
            }
        } catch (CreationException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private DatagramSocket socket;
    private InetAddress serverAdress;
    private int serverPort;
    private final Codec<Packet> codec;
    private final DoubleParamFactory<Packet, Byte, Message> packetFactory;
    private static final AtomicInteger userId = new AtomicInteger(0);
}
