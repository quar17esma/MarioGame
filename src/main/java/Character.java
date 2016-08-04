import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;


public class Character extends Pane{
    Image marioImg = new Image(getClass().getResourceAsStream("mario.png"));  // артинка ћарио
    ImageView imageView = new ImageView(marioImg);

    int count = 3;                                          //кол-во кадров
    int columns = 3;                                        //кол-во колонок(верх, низ, право, лево)

    int offsetX = 96;                                       //координаты первого кадра
    int offsetY = 33;
    int width = 16;                                         //размер кадра
    int height = 16;
    public SpriteAnimation animation;
    public Point2D playerVelocity = new Point2D(0,0);       //точка учета перемещени€ ћарио по Y (гравитаци€)
    private boolean canJump = true;                         //можно ли пригать

    public Character(){                                     //конструктор ћарио
        imageView.setFitHeight(40);                         //измен€ем размер ћарио
        imageView.setFitWidth(40);
        imageView.setViewport(new Rectangle2D(offsetX,offsetY,width,height));  //устанавливаем кадр
                                                                                //создаем анимацию ћарио
        animation = new SpriteAnimation(this.imageView,Duration.millis(200),count,columns,offsetX,offsetY,width,height);
        getChildren().addAll(this.imageView);                                   //св€зываем картинку с персонажем ћарио
    }

    //движение по ’
    public void moveX(int value){
        boolean movingRight = value > 0;                                //вправо при value > 0

        for(int i = 0; i<Math.abs(value); i++) {                        //при каждом смещениии на 1 пиксель
            for (Node block : Game.blocks) {                            //дл€ каждого блока движение и проверка пересичений
                if(this.getBoundsInParent().intersects(block.getBoundsInParent())) {    //если наткнулс€ на блок
                    if (movingRight) {                                  //при движениии вправо отступ на 1 от блока
                        if (this.getTranslateX() + Game.MARIO_SIZE == block.getTranslateX()){
                            this.setTranslateX(this.getTranslateX() - 1);
                            return;
                        }
                    } else {                                            //при движениии вправо отступ на 1 от блока
                        if (this.getTranslateX() == block.getTranslateX() + Game.BLOCK_SIZE) {
                            this.setTranslateX(this.getTranslateX() + 1);
                            return;
                        }
                    }
                }
            }
            this.setTranslateX(this.getTranslateX() + (movingRight ? 1 : -1));      //передвижение вправо или влево на 1
        }
    }

    //движение по Y
    public void moveY(int value){
        boolean movingDown = value > 0;                     //вниз при value > 0

        for(int i = 0; i < Math.abs(value); i++){           //при каждом смещении на 1 пикс.
            for(Block platform : Game.blocks){              //дл€ каждого блока
                if(getBoundsInParent().intersects(platform.getBoundsInParent())){ //при сталкновении с блоком
                    if(movingDown){                 //при движении вниз
                        if(this.getTranslateY()+ Game.MARIO_SIZE == platform.getTranslateY()){ //если упали на блок
                            this.setTranslateY(this.getTranslateY()-1);                        //подимаем ћарио на 1
                            canJump = true;                                                    //можем прыгать
                            return;
                        }
                    } else {                         //при движении вверх
                        if(this.getTranslateY() == platform.getTranslateY()+ Game.BLOCK_SIZE){  //если уперлись головой
                            this.setTranslateY(this.getTranslateY()+1);                         //отступаем на 1 вниз
                            playerVelocity = new Point2D(0,10);                                 //отталкиваем€ вниз на 10 пикс.
                            return;
                        }
                    }
                }
            }

            this.setTranslateY(this.getTranslateY() + (movingDown?1:-1));       //перемещение вниз или вверх на 1 пиксель

            if(this.getTranslateY()>640){            //при падении в пропасть возвращаемс€ в начало
                this.setTranslateX(0);
                this.setTranslateY(400);
                Game.gameRoot.setLayoutX(0);
            }
        }
    }

    //прыжок
    public void jumpPlayer(){
        if(canJump){                                        //если можем прыгать
            playerVelocity = playerVelocity.add(0,-30);     //на 30 пикс. подпрыгиваем вверх
            canJump = false;                                //в полете не можем прыгать
        }
    }
}