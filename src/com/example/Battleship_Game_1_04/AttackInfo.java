package com.example.Battleship_Game_1_04;

/**
 * Created by Jeff Tartt on 3/20/2015.
 */
public class AttackInfo {
    private String userHit;
    private String compRow;
    private String compCol;
    private String compHit;
    private String userShipsSunk;
    private String compShipsSunk;
    private String numCompShipsSunk;
    private String numUserShipsSunk;
    private String winner;

    public AttackInfo(String _userHit, String _compRow, String _compCol,
                      String _compHit, String _userShipsSunk, String _compShipsSunk,
                      String _numCompShipsSunk, String _numUserShipsSunk, String _winner){
        userHit = _userHit;
        compRow = _compRow;
        compCol = _compCol;
        compHit = _compHit;
        userShipsSunk = _userShipsSunk;
        compShipsSunk = _compShipsSunk;
        numCompShipsSunk = _numCompShipsSunk;
        numUserShipsSunk = _numUserShipsSunk;
        winner = _winner;
    }

    public String getUserHit(){
        return userHit;
    }

    public void setUserHit( String _userHit){
        userHit = _userHit;
    }

    public String getCompRow(){
        return compRow;
    }

    public void setCompRow(String _compRow) {
        compRow = _compRow;
    }

    public String getCompCol(){
        return compCol;
    }

    public void setCompCol( String _compCol ){
        compCol = _compCol;
    }

    public String getCompHit(){
        return compHit;
    }

    public void setCompHit(String _compHit) {
        compHit = _compHit;
    }

    public String getUserShipsSunk(){
        return userShipsSunk;
    }

    public void setUserShipsSunk(String _userShipsSunk) {
        userShipsSunk = _userShipsSunk;
    }

    public String getCompShipsSunk(){
        return compShipsSunk;
    }

    public void setCompShipsSunk(String _compShipsSunk) {
        compShipsSunk = _compShipsSunk;
    }

    public String getNumCompShipsSunk(){
        return numCompShipsSunk;
    }

    public void setNumCompShipsSunk(String _numCompShipsSunk) {
        numCompShipsSunk = _numCompShipsSunk;
    }

    public String getNumUserShipsSunk(){
        return numUserShipsSunk;
    }

    public void setNumUserShipsSunk(String _numUserShipsSunk) {
        numUserShipsSunk = _numUserShipsSunk;
    }

    public String getWinner(){
        return winner;
    }

    public void setWinner(String _winner){
        winner = _winner;
    }
}
