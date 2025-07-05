package exceptions;
//cuando no se encuentre
public class ItemNotFound extends Exception {
	public ItemNotFound(String message) {
        super(message);
    }
}