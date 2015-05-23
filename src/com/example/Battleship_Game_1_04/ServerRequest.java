package com.example.Battleship_Game_1_04;

/**
 * Created by Jeff Tartt on 2/13/2015.
 */
public class ServerRequest {
    String url;
    BaseActivity.ServerCommands command;
    Integer resultCode;
    String jsonDataResult;
    String errorString;

    // ServerRequest Constructor
    public ServerRequest( String _url, BaseActivity.ServerCommands _command ) {
        url = _url;
        command = _command;
        resultCode = null;
        jsonDataResult = null;
        errorString = null;
    }

    // url Getter
    public String getUrl() {
        return url;
    }

    // url Setter
    public void setUrl( String _url ) {
        url = _url;
    }

    // command Getter
    public BaseActivity.ServerCommands getCommand() {
        return command;
    }

    // command Setter
    public void setCommand( BaseActivity.ServerCommands _command ) {
        command = _command;
    }

    // resultCode Getter
    public Integer getResultCode() {
        return resultCode;
    }

    // resultCode Setter
    public void setResultCode( Integer _resultCode ) {
        resultCode = _resultCode;
    }

    // jsonDataResult Getter
    public String getJsonDataResult() {
        return jsonDataResult;
    }

    // jsonDataResult Setter
    public void setJsonDataResult( String _jsonDataResult ) {
        jsonDataResult = _jsonDataResult;
    }

    // errorString Getter
    public String getErrorString() {
        return errorString;
    }

    // errorString Setter
    public void setErrorString( String _errorString ) {
        errorString = _errorString;
    }

}
