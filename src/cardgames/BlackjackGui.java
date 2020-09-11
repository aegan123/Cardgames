/*
	GUI parts for BlackJack.
    Copyright (C) 2017  Juhani Vähä-Mäkilä, juhani@fmail.co.uk

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; version 2 of the License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
    */
package src.cardgames;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**GUI definitions for Blackjack.
 * @author Juhani Vähä-Mäkilä
 * @version 0.5
 */
final class BlackjackGui extends GameGui implements ActionListener, WindowListener  {
    /** */
    private static final long serialVersionUID = 1L;
    /**All the buttons needed.*/
    private JButton moreCards, stay, doubleBet, closeButton, split;
    /**Panels to divide the screen into three parts.*/
    private JPanel bottomPanel, middlePanel, topPanel, splitPanel1, splitPanel2;
    /**Different labels needed all over the GUI*/
    private JLabel sumOfPlayer=new JLabel(),sumOfDealer=new JLabel(),valueOfBet=new JLabel(),numOfCredits=new JLabel(), splitSum1, splitSum2;

    public BlackjackGui() {
        init();
    }

    private void init() {
        initiateFrame();
        initiateLabels();
        initiateButtons();
        GameGui.gameFrame.addWindowListener(this);
        GameGui.gameFrame.setVisible(true);
    }
    /**
     * Initiates the gameframe.
     */
    private void initiateFrame() {
        JPanel panel=new JPanel(new BorderLayout());
        GameGui.gameFrame.setContentPane(panel);
        bottomPanel = new JPanel();
        middlePanel=new JPanel();
        topPanel=new JPanel();
        panel.add( bottomPanel, BorderLayout.SOUTH );
        panel.add( middlePanel, BorderLayout.CENTER );
        panel.add( topPanel, BorderLayout.NORTH );

    }

    /**
     * Initiates all the buttons used.
     */
    private void initiateButtons() {
        moreCards=new JButton("More");
        stay=new JButton("Stay");
        doubleBet=new JButton("Double down");
        //closeButton=new JButton("Close game");
        split=new JButton("Split");
        moreCards.addActionListener(this);
        stay.addActionListener(this);
        doubleBet.addActionListener(this);
        //closeButton.addActionListener(this);
        split.addActionListener(this);
        //closeButton.setSize(GameGui.buttonSize);
        moreCards.setActionCommand("1");
        stay.setActionCommand("2");
        doubleBet.setActionCommand("3");
        split.setActionCommand("4");
        doubleBet.setVisible(false);
        split.setVisible(false);
        moreCards.setVisible(false);
        stay.setVisible(false);
        //closeButton.setActionCommand("0");
        //closeButton.setLocation(0, 0);
        bottomPanel.add(moreCards);
        bottomPanel.add(stay);
        bottomPanel.add(doubleBet);
        bottomPanel.add(split);
        //bottomPanel.add(closeButton);
    }
    /**
     * Initiates labels used.
     */
    private void initiateLabels() {
        valueOfBet.setVisible(false);
        bottomPanel.add(new JLabel("Current credits:"));
        bottomPanel.add(numOfCredits);
        bottomPanel.add(new JLabel("Current bet:"));
        bottomPanel.add(valueOfBet);

    }
    //*************************

    /**Asks user for bet to play with.
     * @param credits
     * @return The desired bet.
     */
    double getBet(double credits) {
        double num;
        String in=null;
        while (true){

            while(true){
                in=JOptionPane.showInputDialog("Place your bet.\nYou have "+credits+" credits.");
                if (in==null) {
                    int temp=JOptionPane.showConfirmDialog(GameGui.gameFrame, "Are you sure you want to cancel?", "Cancel?", JOptionPane.YES_NO_OPTION);
                    if (temp==JOptionPane.YES_OPTION) {
                        GameGui.gameFrame.dispose();
                        try {
                            Thread.currentThread().join();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }} else continue;

                }
                break;
            }
            try {
                num=Double.parseDouble(in);
            }
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(GameGui.gameFrame, "Error! Not a number.\nPlease enter a number.");
                continue;
            }
            if (num<0.5) {
                JOptionPane.showMessageDialog(GameGui.gameFrame, "Error!\nYou have bet at least 0.5.");
                continue;
            }
            break;
        }

        return num;
    }
    /**
     * Updates selected part of the gui.
     * @param i 1 = top panel, 2 = middle panel, 3 = bottom panel
     */
    void updateGui(int i) {
        switch (i) {
            case 1:
                topPanel.updateUI();
                break;
            case 2:
                middlePanel.updateUI();
                break;
            case 3:
                bottomPanel.updateUI();
                break;
            default:
                break;
        }

    }
    /**
     * Resets gui to start a new game.
     */
    void resetGui() {
        bottomPanel.updateUI();
        topPanel.removeAll();
        topPanel.updateUI();
        middlePanel.removeAll();
        middlePanel.updateUI();

    }
    /**
     *
     * @param players
     */
    void initialDeal(Player[] players) {
        BufferedImage img = null;
        middlePanel.add(sumOfPlayer);
        topPanel.add(sumOfDealer);
        for (int k=0;k<2;k++) {
            for (int j=0;j<players.length;j++) {
                try {
                    img = ImageIO.read(players[j].getCard(k).getPic());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                middlePanel.add(new JLabel(new ImageIcon(img)));
                middlePanel.updateUI();
            }
            //second card face down.
            try {
                if (k==1) img = ImageIO.read(Cardgames.j1.getCard(k).getBackArt());
                else img = ImageIO.read(Cardgames.j1.getCard(k).getPic());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (k==0) {
                if (Blackjack.hasAce(Cardgames.j1)) {
                    setSumOfDealer("1/11");
                }
                else
                    setSumOfDealer(Blackjack.MAP.get(Cardgames.j1.getCard(k).getRank()).toString());

            }
            topPanel.add(new JLabel(new ImageIcon(img)));
            topPanel.updateUI();

            setSumOfDealer(Integer.toString(Blackjack.valueOfHands[0]));
            topPanel.updateUI();
            //****
            if (Blackjack.hasAce(players[0])) setSumOfPlayer(Blackjack.valueOfHands[1]+"/"+Integer.toString(Blackjack.valueOfHands[1]+10));
            else setSumOfPlayer(Integer.toString(Blackjack.valueOfHands[1]));

            middlePanel.updateUI();
        }

    }
    /**
     *
     * @param msg
     */
    void isCharlie(String msg) {
        moreCards.setVisible(false);
        stay.setVisible(false);
        bottomPanel.updateUI();
        JOptionPane.showMessageDialog(GameGui.gameFrame, msg);

    }
    /**
     * Updates UI after player takes a new card.
     */
    void newCardToPlayer(Player[] players, int[] valueOfHands) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(players[0].getCard(players[0].getNumOfCards()-1).getPic());
        } catch (IOException f) {
            // TODO Auto-generated catch block
            f.printStackTrace();
        }
        middlePanel.add(new JLabel(new ImageIcon(img)));
        setSumOfPlayer(Integer.toString(valueOfHands[1]));
        middlePanel.updateUI();

    }
    /**
     * Updates UI when players hand is >21.
     */
    void over21() {
        moreCards.setVisible(false);
        stay.setVisible(false);
        bottomPanel.updateUI();

    }
    /**
     * Generic message showing method.
     * @param msg The message to show.
     */
    void showMsg(String msg) {
        JOptionPane.showMessageDialog(GameGui.gameFrame, msg);
    }
    /**
     * Displays a yes/no dialog box for player.
     * @param msg1 Actual message text to display.
     * @param title Title of the dialog box.
     * @return True if player answers yes. False otherwise.
     */
    public boolean isYes(String msg1, String title) {
        return JOptionPane.YES_OPTION==JOptionPane.showConfirmDialog(GameGui.gameFrame, msg1, title, JOptionPane.YES_NO_OPTION);
    }
    /**
     * Shows an input dialog and returns what the user enters.
     * @param msg The message to display.
     * @return User inputted string.
     */
    String showInputDialog(String msg) {
        return JOptionPane.showInputDialog(msg);
    }
    /**
     * Updates UI on dealers turn.
     * @param b True = first round after player. Makes the second dealt card visible. False = remaining rounds of dealers turn.
     * @param valueOfHand Current value of dealers hand.
     */
    void dealersTurn(boolean b, int valueOfHand) {
        if(b) {
            topPanel.remove(2);
            try {
                topPanel.add(new JLabel(new ImageIcon(ImageIO.read(Cardgames.j1.getCard(1).getPic()))));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            setSumOfDealer(Integer.toString(valueOfHand));
            topPanel.updateUI();
        }
        else {
            BufferedImage img=null;
            try {
                img = ImageIO.read(Cardgames.j1.getCard(Cardgames.j1.getNumOfCards()-1).getPic());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            setSumOfDealer(Integer.toString(valueOfHand));
            topPanel.add(new JLabel(new ImageIcon(img)));
            topPanel.updateUI();
        }
    }
    /**
     * Updates UI when dealer has a Blackjack.
     */
    void dealersBlackjack() {
        setSumOfDealer("Blackjack!");
        topPanel.updateUI();
    }
    /**
     * Updates UI when player has a Blackjack.
     */
    void playersBlackjack() {
        setMoreCards(false);
        setStay(false);
        setDoubleBet(false);
        setSumOfPlayer("Blackjack!");
        middlePanel.updateUI();

    }
    /**
     * Updates UI when player doesn't take any more cards.
     */
    void noMoreCards() {
        doubleBet.setVisible(false);
        moreCards.setVisible(false);
        stay.setVisible(false);
        bottomPanel.updateUI();

    }
    /**
     * Updates UI when player doubles the bet.
     */
    void doubleBet(double bet) {
        valueOfBet.setText(Double.toString(bet));
        doubleBet.setVisible(false);
        moreCards.setVisible(false);
        stay.setVisible(false);
        bottomPanel.updateUI();

    }
    //******************
    //Getters & Setters*
    //******************
    void setMoreCards(boolean b) {
        this.moreCards.setVisible(b);
    }

    void setStay(boolean b) {
        this.stay.setVisible(b);
    }

    void setDoubleBet(boolean b) {
        this.doubleBet.setVisible(b);
    }

    void setCloseButton(JButton closeButton) {
        this.closeButton = closeButton;
    }

    void setSplit(boolean b) {
        this.split.setVisible(b);
    }

    void setBottomPanel(JPanel bottomPanel) {
        this.bottomPanel = bottomPanel;
    }

    void setMiddlePanel(JPanel middlePanel) {
        this.middlePanel = middlePanel;
    }

    void setTopPanel(JPanel topPanel) {
        this.topPanel = topPanel;
    }

    void setSumOfPlayer(String label) {
        this.sumOfPlayer.setText(label);
    }

    void setSumOfDealer(String label) {
        this.sumOfDealer.setText(label);
    }

    void setValueOfBet(String label) {
        this.valueOfBet.setText(label);
    }
    void setValueOfBet(boolean b) {
        this.valueOfBet.setVisible(b);
    }

    void setNumOfCredits(String label) {
        this.numOfCredits.setText(label);;
    }
    void setNumOfCredits(boolean b) {
        this.numOfCredits.setVisible(b);
    }

    //*****************
    //Window listeners*
    //*****************
    @Override
    public void windowClosing(WindowEvent e) {
        Blackjack.closeGame();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        //Do something based on button pressed.
        switch (Integer.parseInt(e.getActionCommand())) {
            case(0):
                //Close game.
                Blackjack.closeGame();
                break;
            case(1):
                //Player takes more cards.
                doubleBet.setVisible(false);
                Blackjack.moreCards();
                break;
            case(2):
                //player doesn't take more cards.
                Blackjack.noMoreCards();
                break;
            case(3):
                //Doubles the bet + one card
                Blackjack.doubleBet();
                break;
            case(4):
                //TODO Split the deck
                Blackjack.splitTheHand();
                break;
            default:
                break;
        }

    }
    //**********
    //Not used.*
    //**********
    @Override
    public void windowOpened(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowClosed(WindowEvent e) {
        //TODO Auto-generated method stub

    }


    @Override
    public void windowIconified(WindowEvent e) {
        // TODO Auto-generated method stub

    }


    @Override
    public void windowDeiconified(WindowEvent e) {
        // TODO Auto-generated method stub

    }


    @Override
    public void windowActivated(WindowEvent e) {
        // TODO Auto-generated method stub

    }


    @Override
    public void windowDeactivated(WindowEvent e) {
        // TODO Auto-generated method stub

    }
    /**
     *
     * @param b True=first part of split hands. False=second part of split hands.
     * @param card
     */
    void splitHand(boolean b, Card card, int valueOfHand) {
        if(b) {
            splitPanel1=new JPanel();
            splitPanel2=new JPanel();
            middlePanel.removeAll();
            middlePanel.setLayout(new BorderLayout());
            middlePanel.add(splitPanel1, BorderLayout.WEST);
            middlePanel.add(splitPanel2, BorderLayout.EAST);
            middlePanel.updateUI();
            splitSum1=new JLabel();
            try {
                splitPanel1.add(new JLabel(new ImageIcon(getImg(card))));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else {
            splitSum2=new JLabel();
        }

    }

    private BufferedImage getImg(Card card) throws IOException {
        // TODO Auto-generated method stub
        return ImageIO.read(card.getPic());
    }


}
