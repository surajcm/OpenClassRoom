function listMe() {
    document.forms[0].action="/user/ListAll.htm";
    document.forms[0].submit();
}

function goToHome() {
    document.forms[0].action="/user/ToHome.htm";
    document.forms[0].submit();
}

function logMeOut() {
    document.forms[0].method = "GET";
    document.forms[0].action="/user/logMeOut";
    document.forms[0].submit();
}