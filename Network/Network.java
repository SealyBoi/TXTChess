package Network;

public abstract class Network {
    // Just to assign clients and servers as the same
    public abstract void run() throws Exception;

    public abstract String[] readInput() throws Exception;

    public abstract void sendOutput(String[] move) throws Exception;

    public abstract void closeNetwork() throws Exception;
}
