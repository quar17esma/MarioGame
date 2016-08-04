import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;


public class Character extends Pane{
    Image marioImg = new Image(getClass().getResourceAsStream("mario.png"));  //�������� �����
    ImageView imageView = new ImageView(marioImg);

    int count = 3;                                          //���-�� ������
    int columns = 3;                                        //���-�� �������(����, ���, �����, ����)

    int offsetX = 96;                                       //���������� ������� �����
    int offsetY = 33;
    int width = 16;                                         //������ �����
    int height = 16;
    public SpriteAnimation animation;
    public Point2D playerVelocity = new Point2D(0,0);       //����� ����� ����������� ����� �� Y (����������)
    private boolean canJump = true;                         //����� �� �������

    public Character(){                                     //����������� �����
        imageView.setFitHeight(40);                         //�������� ������ �����
        imageView.setFitWidth(40);
        imageView.setViewport(new Rectangle2D(offsetX,offsetY,width,height));  //������������� ����
                                                                                //������� �������� �����
        animation = new SpriteAnimation(this.imageView,Duration.millis(200),count,columns,offsetX,offsetY,width,height);
        getChildren().addAll(this.imageView);                                   //��������� �������� � ���������� �����
    }

    //�������� �� �
    public void moveX(int value){
        boolean movingRight = value > 0;                                //������ ��� value > 0

        for(int i = 0; i<Math.abs(value); i++) {                        //��� ������ ��������� �� 1 �������
            for (Node block : Game.blocks) {                            //��� ������� ����� �������� � �������� �����������
                if(this.getBoundsInParent().intersects(block.getBoundsInParent())) {    //���� ��������� �� ����
                    if (movingRight) {                                  //��� ��������� ������ ������ �� 1 �� �����
                        if (this.getTranslateX() + Game.MARIO_SIZE == block.getTranslateX()){
                            this.setTranslateX(this.getTranslateX() - 1);
                            return;
                        }
                    } else {                                            //��� ��������� ������ ������ �� 1 �� �����
                        if (this.getTranslateX() == block.getTranslateX() + Game.BLOCK_SIZE) {
                            this.setTranslateX(this.getTranslateX() + 1);
                            return;
                        }
                    }
                }
            }
            this.setTranslateX(this.getTranslateX() + (movingRight ? 1 : -1));      //������������ ������ ��� ����� �� 1
        }
    }

    //�������� �� Y
    public void moveY(int value){
        boolean movingDown = value > 0;                     //���� ��� value > 0

        for(int i = 0; i < Math.abs(value); i++){           //��� ������ �������� �� 1 ����.
            for(Block platform : Game.blocks){              //��� ������� �����
                if(getBoundsInParent().intersects(platform.getBoundsInParent())){ //��� ������������ � ������
                    if(movingDown){                 //��� �������� ����
                        if(this.getTranslateY()+ Game.MARIO_SIZE == platform.getTranslateY()){ //���� ����� �� ����
                            this.setTranslateY(this.getTranslateY()-1);                        //�������� ����� �� 1
                            canJump = true;                                                    //����� �������
                            return;
                        }
                    } else {                         //��� �������� �����
                        if(this.getTranslateY() == platform.getTranslateY()+ Game.BLOCK_SIZE){  //���� �������� �������
                            this.setTranslateY(this.getTranslateY()+1);                         //��������� �� 1 ����
                            playerVelocity = new Point2D(0,10);                                 //������������ ���� �� 10 ����.
                            return;
                        }
                    }
                }
            }

            this.setTranslateY(this.getTranslateY() + (movingDown?1:-1));       //����������� ���� ��� ����� �� 1 �������

            if(this.getTranslateY()>640){            //��� ������� � �������� ������������ � ������
                this.setTranslateX(0);
                this.setTranslateY(400);
                Game.gameRoot.setLayoutX(0);
            }
        }
    }

    //������
    public void jumpPlayer(){
        if(canJump){                                        //���� ����� �������
            playerVelocity = playerVelocity.add(0,-30);     //�� 30 ����. ������������ �����
            canJump = false;                                //� ������ �� ����� �������
        }
    }
}