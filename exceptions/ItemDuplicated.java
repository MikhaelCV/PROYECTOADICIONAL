package exceptions;
//cuando se duplique 
public class ItemDuplicated extends Exception {
    public ItemDuplicated(String message) {
        super(message);
    }
}