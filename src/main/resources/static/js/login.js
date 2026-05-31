function validateForm(){
    const x=document.forms["frm"]["username"].value;
    if(x==" "){
        alert("Username must not to be blank.");
        return false;
    }
    //const y=document.forms["frm"]["password"].value;
    /*if(y==" "){
        alert("Password should no blank.");
        return false;
    }*/
}