package mine_game;


import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.ArrayList;


class View extends JFrame{
    
    private static final long serialVersionUID = 1L;
    
    Label[][] labels;
    Label[] moves;
    Label status;//状況を表すラベル
    Label substatus;//状況を表すラベル2
    JButton buttonLeft,buttonRight,buttonUp,buttonDown,
    buttonLeftUp,buttonRightUp,buttonLeftDown,buttonRightDown,
    buttonReset;
    JPanel movePanel ; // 移動数用パネル
    Label[] moveCount ; // 移動数用パネル

    int height = 5; //縦の大きさ
    int width = 5; // 横の大きさ
    int heightPosition = height/2; //現在いる場所の縦(●)を確認する変数、初期値は中央
    int widthPosition = width/2; //現在いる場所の横(●)を確認する変数、初期値は中央
    int clearMove = 5;
    int mineNuber = 5;
    boolean game = true;
    ArrayList<Position> visited = new ArrayList<Position>(); //過去に移動した場所を保管するList
    ArrayList<Position> mine = new ArrayList<Position>();

    public void SetMine(){//地雷を設置
        do {
            Position tempMine = new Position((int) (Math.random() * height),(int) (Math.random() * width));
            if (!positionCheck(visited,tempMine.getHeight(),tempMine.getWidth())) { // 初期値に一致しないか判断
                if (!positionCheck(mine,tempMine.getHeight(),tempMine.getWidth())) { //初期値と設置済みに一致しなければListに追加
                    mine.add(tempMine);
                }
            }
        } while (mine.size() < mineNuber);
    }

    public void mineDisplay(Label[][] labels){//地雷を表示
        game = false;
        for (int i = 0; i < labels.length;i++) { //回答
            for (int j = 0;j < labels[i].length;j++) {
                if (positionCheck(mine,i,j)) {
                    labels[i][j].setText("★");
                }
                if (positionCheck(mine,heightPosition,widthPosition)) {
                    labels[heightPosition][widthPosition].setText("(✬)");
                } 
            }
        }
    }
    public void Reset() {// ゲームをリセットする
        if (!game) { //gameが終了しているfalseのときに動作
            game = true;
            for (int i = 0; i < labels.length;i++) { //すべて○に戻す
                for (int j = 0;j < labels[i].length;j++) {
                    labels[i][j].setText("○");
                    }
                }
            heightPosition = height/2; //現在いる場所を中央にする
            widthPosition = width/2;
            substatus.setText("Start!"); //メッセージを戻す
            status.setText("地雷を踏まずに5回進みましょう!"); //メッセージを戻す
            positionCurrent();
            visited.clear(); //移動した場所のListを削除
            visited.add(new Position(heightPosition,widthPosition));
            moveCurrent(); //移動◆表示
            mine.clear(); // 地雷のList削除
            SetMine();//地雷を設置
        }
    }

    public void buttoncheck(boolean game) {
        buttonLeft.setEnabled(game);// ボタンリセット
        buttonRight.setEnabled(game);
        buttonUp.setEnabled(game);
        buttonDown.setEnabled(game);
        buttonLeftUp.setEnabled(game);// ボタンリセット
        buttonRightUp.setEnabled(game);
        buttonLeftDown.setEnabled(game);
        buttonRightDown.setEnabled(game);
        buttonReset.setEnabled(!game);
    }

    public void moveCurrent() {  //移動回数◆の表示
      for (int i = 0; i < clearMove; i++) {
          if (i >= visited.size()-1){
              moveCount[i].setText("");//移動数を表示
          } else {
              moveCount[i].setText("◆");//移動数を表示
          }
        }
    }

    public void positionCurrent() {  //●表示と移動チェックメソッド
        if(game) { //game状況チェック
            labels[heightPosition][widthPosition].setText("●"); //現在地を●に変える
        }
    }

    public void gameEnd() {  //●終了時メソッド
        mineDisplay(labels); // 地雷表示
        buttoncheck(game);
    }

    public void positionDisplay() {  //●表示と移動チェックメソッド
        if(game) { //game状況チェック
            positionCurrent(); // 現在地を●に変更
            if (positionCheck(visited)) { ////現在位置と過去移動Listを照合
                substatus.setText("safety!"); //過去に一致していれば移動済み表示
                status.setText("ここは確認済みです。"); //過去に一致していれば移動済み表示
            } else if (positionCheck(mine)){ // 地雷チェック
                substatus.setText("BooooooN!!"); //過去に一致していれば移動済み表示
                status.setText("残念、地雷を踏んでしまいました!"); //過去に一致していれば移動済み表示
                gameEnd(); //地雷表示とボタンリセット
                //終了プログラムを入れる予定　            System.exit(0);
            } else {//過去に一致していれば移動済み表示
                substatus.setText("Good!!"); //過去に一致なければvisitedListに追加
                status.setText("地雷はありませんでした。"); //メッセージ表示
                visited.add(new Position(heightPosition,widthPosition)); //現在地をリストに追加
                moveCurrent(); //移動◇◆表示
            }
            if (visited.size()-1 >= clearMove) { //一致せずクリア数に達したらクリア
                substatus.setText("Clear!!");
                status.setText("無事に" + String.valueOf(visited.size()-1) + "歩進みました！"); //過去に一致していれば移動済み表示
                gameEnd(); //地雷表示とボタンリセット
            }
            
        }
        buttoncheck(game); //ボタン確認
    }

    public int move(boolean conditions,int moveValue,int direction) { //移動可否判別メソッド(移動範囲条件,移動値)
        if(game) {
            labels[heightPosition][widthPosition].setText("○"); //現在の場所を〇に戻す
            if (conditions) {
                return moveValue; //範囲内なら移動値を返す
            } else {
                return moveValue * (-1 * (direction-1)); //範囲外なら縦横の大きさ分増減
            }
        }
        return 0; //ゲームオフなら移動値0
    }

    public void moveLeft() { //左移動メソッド
        widthPosition += move(widthPosition > 0,-1,width);  //移動判別メソッド呼び出し
    }

    public void moveRight() {// 右移動メソッド
        widthPosition += move(widthPosition < width-1,1,width);  //移動判別メソッド呼び出し
    }

    public void moveUp() {// 上移動メソッド
        heightPosition += move(heightPosition > 0,-1,height);  //移動判別メソッド呼び出し
    }

    public void moveDown() {// 下移動メソッド
        heightPosition += move(heightPosition < height-1,1,height);  //移動判別メソッド呼び出し
    }
    
    public boolean positionCheck(ArrayList<Position> checkPosition) { // 移動場所の状況チェックメソッド、指定なければ現在位置として引き継ぎ
        return positionCheck(checkPosition,heightPosition,widthPosition); 
        }

    public boolean positionCheck(ArrayList<Position> checkPosition,int height,int width) { // 移動場所の状況チェックメソッド、引数位置
        for(int i = 0; i <checkPosition.size();i++) {
            if (checkPosition.get(i).getHeight() == height && checkPosition.get(i).getWidth()== width ) { //過去の場所か判別
                return true; //過去に一致していれば true
            }
        }
        return false; //過去に一致なければfalse
    }
    


    public View(String title) {
        setTitle(title);
        setBounds(100,100,800,800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        labels = new Label[height][width]; // 〇の位置情報用配列生成
        for (Label[] label:labels) {
            for (int i = 0;i < label.length;i++) {
                label[i] = new Label("○");
            }
        }
        Position startPosition = new Position(heightPosition,widthPosition); // スタート地点を移動済みに追加
        visited.add(startPosition);
        SetMine();//地雷を設置

        JPanel panel1 = new JPanel(); // 〇配列設置用パネル
        panel1.setLayout(new GridLayout(height,width)); //ラベルサイズ分レイアウト作成

        for (Label[] label:labels) { //ラベルをパネルに代入
            for (Label labelIn:label) {
                panel1.add(labelIn);
                labelIn.setAlignment(Label.CENTER); //ラベルを中央揃え
            }
        }
        

        buttonLeft = new JButton("←"); //移動ボタン
        buttonRight = new JButton("→");
        buttonUp = new JButton("↑");
        buttonDown = new JButton("↓");
        buttonLeftUp = new JButton("↖");
        buttonRightUp = new JButton("↗");
        buttonLeftDown = new JButton("↙");
        buttonRightDown = new JButton("↘");
        buttonReset = new JButton("リセット");
        JPanel panel2 = new JPanel();

        panel2.setLayout(new GridLayout(3,3)); // ボタン配置パネル
        panel2.add(buttonLeftUp); //左上追加
        panel2.add(buttonUp); //上追加
        panel2.add(buttonRightUp); //右上
        panel2.add(buttonLeft); //左追加
        panel2.add(buttonReset); //5 移動した回数
        panel2.add(buttonRight); //右追加
        panel2.add(buttonLeftDown); //左下追加
        panel2.add(buttonDown); //下側にbuttonRightを追加
        panel2.add(buttonRightDown); //右下にbuttonRightDownを追加

        ActionListener action1 = new ActionListener() { //左ボタンアクション
            @Override
            public void actionPerformed(ActionEvent e) {
                moveLeft();
                positionDisplay(); //移動後●表示
            }
        };
        ActionListener action2 = new ActionListener() { //右ボタンアクション
            @Override
            public void actionPerformed(ActionEvent e) {
                moveRight();
                positionDisplay(); //移動後●表示
            }
        };
        ActionListener action3 = new ActionListener() { //上ボタンアクション
            @Override
            public void actionPerformed(ActionEvent e) {
                moveUp();
                positionDisplay(); //移動後●表示
            }
        };
        ActionListener action4 = new ActionListener() { //下ボタンアクション
            @Override
            public void actionPerformed(ActionEvent e) {
                moveDown();
                positionDisplay(); //移動後●表示
            }
        };
        ActionListener action5 = new ActionListener() { //左上ボタンアクション
            @Override
            public void actionPerformed(ActionEvent e) {
                moveLeft();
                moveUp();
                positionDisplay(); //移動後●表示
            }
        };
        ActionListener action6 = new ActionListener() { //右上ボタンアクション
            @Override
            public void actionPerformed(ActionEvent e) {
                moveRight();
                moveUp();
                positionDisplay(); //移動後●表示
            }
        };
        ActionListener action7 = new ActionListener() { //左下ボタンアクション
            @Override
            public void actionPerformed(ActionEvent e) {
                moveLeft();
                moveDown();
                positionDisplay(); //移動後●表示
            }
        };
        ActionListener action8 = new ActionListener() { //右下ボタンアクション
            @Override
            public void actionPerformed(ActionEvent e) {
                moveRight();
                moveDown();
                positionDisplay(); //移動後●表示
            }
        };
        ActionListener action9 = new ActionListener() { //リセットボタンアクション
            @Override
            public void actionPerformed(ActionEvent e) {
                Reset() ;
                buttoncheck(game);
                }
        };
        buttonLeft.addActionListener(action1); //左側ボタンaction1
        buttonRight.addActionListener(action2); //右側ボタンaction2
        buttonUp.addActionListener(action3); //左側ボタンaction3
        buttonDown.addActionListener(action4); //右側ボタンaction4
        buttonLeftUp.addActionListener(action5); //左上ボタンaction1
        buttonRightUp.addActionListener(action6); //右上ボタンaction2
        buttonLeftDown.addActionListener(action7); //左下ボタンaction3
        buttonRightDown.addActionListener(action8); //右下ボタンaction4
        buttonReset.addActionListener(action9);

        JPanel explanationPanel = new JPanel(); // 状況説明用パネル
        explanationPanel.setLayout(new GridLayout(2,1)); //ラベルサイズ分レイアウト作成

        substatus= new Label("Start!"); //移動回数表示
        status= new Label("地雷を踏まずに5回進みましょう!"); //移動回数表示
        explanationPanel.add(substatus);
        explanationPanel.add(status);
        substatus.setAlignment(Label.CENTER); //ラベルを中央揃え
        status.setAlignment(Label.CENTER); //ラベルを中央揃え

        movePanel = new JPanel(); // 移動数用パネル
        moveCount = new Label[clearMove]; // 移動数用パネル
        for (int i = 0; i < clearMove; i++) {
          if (i >= visited.size()-1){
              moveCount[i] = new Label("");
          } else {
              moveCount[i] = new Label("◆");
          }
          moveCount[i].setAlignment(Label.CENTER); //ラベルを中央揃え
        }
        for (int i = 0; i < clearMove; i++) {
            movePanel.add(moveCount[i]);
        }
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(5,3)); //レイアウトセット

        contentPane.add(new Label()); //1 空欄
        contentPane.add(new Label()); //2 空欄
        contentPane.add(new Label()); //3 空欄
        contentPane.add(new Label()); //4 空欄
        contentPane.add(panel1); //5
        contentPane.add(new Label()); //6 空欄
        contentPane.add(new Label()); //7 空欄
        contentPane.add(explanationPanel); //8 空欄
        contentPane.add(new Label()); //9 空欄
        contentPane.add(new Label()); //10 空欄
        contentPane.add(panel2); //11
        contentPane.add(new Label("")); //12 空欄
        contentPane.add(new Label("")); //13 空欄
        contentPane.add(movePanel); //14 空欄
        contentPane.add(new Label("")); //15 空欄
        positionCurrent(); //移動後の場所を●に変える
        buttoncheck(game); //ボタンチェック
        setVisible(true); //表示
    }
}


public class mine_game {

    public static void main(String[] args) {
        new View("avoid mines");
        
    }

}
