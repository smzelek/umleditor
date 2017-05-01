import com.canisdev.UMLClassBox;
import com.canisdev.UMLEditor;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Kelez on 4/4/2017.
 */
public class SimpleBoxTests extends ApplicationTest {

    UMLEditor root;
    @Override
    public void start(Stage stage) throws Exception {
        root = new UMLEditor(stage);
        Scene scene = new Scene(root, 400,450);
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void makeABox()
    {
        clickOn("#newBoxBtn");
        clickOn("#umlArea");
        UMLClassBox umlClassBox = lookup("#uml-class-box-frame").query();
        assertNotNull("Did not create a box", umlClassBox);
        System.out.println("Box created at position (" + umlClassBox.getTranslateX() + ", " + umlClassBox.getTranslateY() + ")");
        assertEquals("Box not added to umlArea's list", root.getUmlArea().getChildren().size(), 1);
        System.out.println("Box added to umlArea's list");
    }

    @Test
    public void resizeBoxTop()
    {
        clickOn("#newBoxBtn");
        clickOn("#umlArea");
        UMLClassBox umlClassBox = lookup("#uml-class-box-frame").query();
        clickOn(umlClassBox.top);
        drag(umlClassBox.top).moveBy(0, -10);
    }

}