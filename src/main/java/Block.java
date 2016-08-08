import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

//класс возможных обьектов(блоков) на уровне
public class Block extends Pane {
                                                                    //общее изображение
    Image blocksImg = new Image(getClass().getResourceAsStream("platforms.png"));
    ImageView block;                                                //блок

    public enum BlockType {                                         //возможные блоки
        PLATFORM, BRICK, BONUS, PIPE_TOP, PIPE_BOTTOM, INVISIBLE_BLOCK, STONE
    }

    public Block(BlockType blockType, int x, int y) {               //конструктор блока
        block = new ImageView(blocksImg);

        block.setFitWidth(Game.BLOCK_SIZE);                         //размер блока
        block.setFitHeight(Game.BLOCK_SIZE);

        setTranslateX(x);                                           //координаты блока
        setTranslateY(y);

        switch (blockType) {                                        //изображение блока в зависимости от типа
            case PLATFORM:
                block.setViewport(new Rectangle2D(0, 0, 16, 16));
                break;
            case BRICK:
                block.setViewport(new Rectangle2D(16, 0, 16, 16));
                break;
            case BONUS:
                block.setViewport(new Rectangle2D(384, 0, 16, 16));
                break;
            case PIPE_TOP:
                block.setViewport(new Rectangle2D(0, 128, 32, 16));
                block.setFitWidth(Game.BLOCK_SIZE * 2);     //двойная ширина для трубы
                break;
            case PIPE_BOTTOM:
                block.setViewport(new Rectangle2D(0, 145, 32, 14));
                block.setFitWidth(Game.BLOCK_SIZE * 2);     //двойная ширина для трубы
                break;
            case INVISIBLE_BLOCK:
                block.setViewport(new Rectangle2D(0, 0, 16, 16));
                block.setOpacity(0);
                break;
            case STONE:
                block.setViewport(new Rectangle2D(0, 16, 16, 16));
                break;
        }
                                                                    //добавляем блок на уровень
        getChildren().add(block);
        Game.blocks.add(this);
        Game.gameRoot.getChildren().add(this);
    }
}



