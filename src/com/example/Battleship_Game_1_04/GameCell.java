package com.example.Battleship_Game_1_04;

import android.graphics.Point;

/**
 * Created by Jeff Tartt on 2/28/2015.
 */
public class GameCell {
    private Boolean has_ship;
    private Boolean miss;
    private Boolean hit;
    private Boolean waiting;
    private Point topLeft;
    private Point bottomRight;
    private Point viewOrigin;
    private int cellHeight, cellWidth;

    public GameCell(){
        has_ship = false;
        miss = false;
        hit = false;
        waiting = false;
        topLeft = null;
        bottomRight = null;
        cellHeight = 0;
        cellWidth = 0;
    }

    public GameCell( Boolean _has_ship, Boolean _miss, Boolean _hit, Boolean _waiting, Point _topLeft, Point _bottomRight ) {
        has_ship = _has_ship;
        miss = _miss;
        hit = _hit;
        waiting = _waiting;
        topLeft = _topLeft;
        bottomRight = _bottomRight;
    }

    public void setViewOrigin( Point _origin ) {
        viewOrigin = _origin;
    }

    public Point getViewOrigin() {
        return viewOrigin;
    }

    public void setCellHeight( int _cellHeight ) {
        cellHeight = _cellHeight;
    }

    public int getCellHeight() {
        return cellHeight;
    }

    public void setCellWidth( int _cellWidth ) {
        cellWidth = _cellWidth;
    }

    public int getCellWidth() {
        return cellWidth;
    }

    public Boolean getHas_ship() {
        return has_ship;
    }

    public void setHas_ship( Boolean _has_ship ) {
        has_ship = _has_ship;
    }

    public Boolean getMiss() {
        return miss;
    }

    public void setMiss( Boolean _miss ) {
        miss = _miss;
    }

    public Boolean getHit() {
        return hit;
    }

    public void setHit( Boolean _hit ) {
        hit = _hit;
    }

    public Boolean getWaiting() {
        return waiting;
    }

    public void setWaiting( Boolean _waiting ) {
        waiting = _waiting;
    }

    public Point getTopleft() {
        return topLeft;
    }

    public void setTopleft( Point _topLeft ) {
        topLeft = _topLeft;
    }

    public Point getBottomright() {
        return bottomRight;
    }

    public void setBottomright( Point _bottomRight ) {
        bottomRight = _bottomRight;
    }
}
