function listMe(){
    document.forms[0].action="/user/ListAll.htm";
    document.forms[0].submit();
}

function goToHome(){
    document.forms[0].action="/user/ToHome.htm";
    document.forms[0].submit();
}

function LogMeOut(){
    document.forms[0].action="/user/LogMeOut.htm";
    document.forms[0].submit();
}