/**
 * Created by Quar17esma on 20.07.2016.
 */

import javafx.animation.AnimationTimer;
import  javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.lang.*;
import java.util.ArrayList;
import java.util.HashMap;

//главный класс программы
public class Game extends Application {
    public static ArrayList<Block> blocks = new ArrayList<>();      //храним блоки
    private HashMap<KeyCode, Boolean> keys = new HashMap<>();       //храник коды кнопок.

                                                                    //фоновый рисунок
    Image backgroundImg = new Image(getClass().getResourceAsStream("background.png"));
    public static final int BLOCK_SIZE = 45;                        //размер блока
    public static final int MARIO_SIZE = 40;                        //размер Марио

    public static Pane appRoot = new Pane();                        //главная панель
    public static Pane gameRoot = new Pane();                       //панель игрового процесса

    public Character player;                                        //персонаж
    int levelNumber = 0;                                            //уровень
    private int levelWidth;                                         //длинна уровня

    private void initContent(){
        ImageView background = new ImageView(backgroundImg);        //фон
        background.setFitHeight(14*BLOCK_SIZE);                     //размер фона 212х14 блоков
        background.setFitWidth(212*BLOCK_SIZE);

        levelWidth = LevelData.levels[levelNumber][0].length()*BLOCK_SIZE;  //длинна первой стороки в первом уровне * на размер блока
        for(int i = 0; i < LevelData.levels[levelNumber].length; i++){      //проходим по масиву строк в уровне и создаем
                                                                            //соответствующие блоки, усанавливаем их координаты
            for(int j = 0; j < LevelData.levels[levelNumber][i].length();j++){
                switch (LevelData.levels[levelNumber][i].charAt(j)){
                    case '0':
                        break;
                    case '1':
                        Block platformFloor = new Block(Block.BlockType.PLATFORM, j * BLOCK_SIZE, i * BLOCK_SIZE);
                        break;
                    case '2':
                        Block brick = new Block(Block.BlockType.BRICK,j*BLOCK_SIZE,i*BLOCK_SIZE);
                        break;
                    case '3':
                        Block bonus = new Block(Block.BlockType.BONUS,j*BLOCK_SIZE,i*BLOCK_SIZE);
                        break;
                    case '4':
                        Block stone = new Block(Block.BlockType.STONE,j * BLOCK_SIZE, i * BLOCK_SIZE);
                        break;
                    case '5':
                        Block PipeTopBlock = new Block(Block.BlockType.PIPE_TOP,j * BLOCK_SIZE, i * BLOCK_SIZE);
                        break;
                    case '6':
                        Block PipeBottomBlock = new Block(Block.BlockType.PIPE_BOTTOM,j * BLOCK_SIZE, i * BLOCK_SIZE);
                        break;
                    case '*':
                        Block InvisibleBlock = new Block(Block.BlockType.INVISIBLE_BLOCK,j * BLOCK_SIZE, i * BLOCK_SIZE);
                        break;
                }
            }
        }

        player = new Character();               //создаем Марио
        player.setTranslateX(0);                //устанавливаем его координаты
        player.setTranslateY(400);

        player.translateXProperty().addListener((obs,old,newValue)->{   //слушатель, при смещении Марио по X - фон сдвигается
            int offset = newValue.intValue();
            if(offset>640 && offset<levelWidth-640){
                gameRoot.setLayoutX(-(offset-640));
                background.setLayoutX(-(offset-640));
            }
        });

        gameRoot.getChildren().add(player);                 //добавляем персонажа в игру
        appRoot.getChildren().addAll(background,gameRoot);  //добавляем фон и игру в главное окно
    }

    //
    private void update(){
        if(isPressed(KeyCode.UP) && player.getTranslateY()>=5){     //прижок при нажатии ВВЕРХ
            player.jumpPlayer();
        }
        if(isPressed(KeyCode.LEFT) && player.getTranslateX()>=5){   //движение влево и запуск анимации при нажатии ВЛЕВО
            player.setScaleX(-1);
            player.animation.play();
            player.moveX(-5);
        }                                                           //движение вправо и запуск анимации при нажатии ВПРАВО
        if(isPressed(KeyCode.RIGHT) && player.getTranslateX()+40 <=levelWidth-5){
            player.setScaleX(1);
            player.animation.play();
            player.moveX(5);
        }                                                           //гравитация
        if(player.playerVelocity.getY()<10){                        //если Марио в прижке - падаем на 1 пикс. вниз
           player.playerVelocity = player.playerVelocity.add(0,1);
        }
        player.moveY((int)player.playerVelocity.getY());            //перемещаем Марио по Y (при прыжке или падении)
    }

    private boolean isPressed(KeyCode key){
        return keys.getOrDefault(key,false);                        //нажата кнопка или нет (не нажата по-умолчанию)
    }

    @Override
    public void start(Stage primaryStage) throws Exception {        //заупуск приложения
        initContent();                                              //инициализируем контент

        Scene scene = new Scene(appRoot,1200,620);                  //устанавливаем размер сцены
        scene.setOnKeyPressed(event->
            keys.put(event.getCode(), true));                       //слушатель на нажатие кнопок
        scene.setOnKeyReleased(event -> {                           //слушатель на отпускание кнопок
            keys.put(event.getCode(), false);
            player.animation.stop();
        });

        primaryStage.setTitle("Mini Mario");                        //название окна
        primaryStage.setScene(scene);                               //добавляем сцену в окно
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {               //устанавливаем и запускаем таймер анимации
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
