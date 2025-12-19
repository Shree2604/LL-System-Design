// Receiver - performs the actual operations
class Document {
    public void open() {
        System.out.println("Document opened");
    }
    
    public void save() {
        System.out.println("Document saved");
    }
}

// Command Interface
interface ActionListenerCommand {
    void execute();
}

// Concrete Command - Open
class ActionOpen implements ActionListenerCommand {
    private Document document;
    
    public ActionOpen(Document document) {
        this.document = document;
    }
    
    @Override
    public void execute() {
        document.open();
    }
}

// Concrete Command - Save
class ActionSave implements ActionListenerCommand {
    private Document document;
    
    public ActionSave(Document document) {
        this.document = document;
    }
    
    @Override
    public void execute() {
        document.save();
    }
}

// Invoker
class MenuOptions {
    private ActionListenerCommand openCommand;
    private ActionListenerCommand saveCommand;
    
    public MenuOptions(ActionListenerCommand openCommand, ActionListenerCommand saveCommand) {
        this.openCommand = openCommand;
        this.saveCommand = saveCommand;
    }
    
    public void clickOpen() {
        openCommand.execute();
    }
    
    public void clickSave() {
        saveCommand.execute();
    }
}

// Client
public class DocumentDemo {
    public static void main(String[] args) {
        // Receiver - performing action
        Document doc = new Document();
        
        // Create concrete commands
        // Receiver with command
        ActionListenerCommand clickOpen = new ActionOpen(doc);
        ActionListenerCommand clickSave = new ActionSave(doc);
        
        // Invoker
        MenuOptions menu = new MenuOptions(clickOpen, clickSave);
        
        // Execute commands through invoker
        menu.clickOpen();
        menu.clickSave();
    }
}