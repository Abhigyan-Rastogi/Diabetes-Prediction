function handleForm() {
    let form = new FormData(document.getElementById("theform"));
    let form_data = "";
    for(const pair of form.entries()) {
        form_data += pair[0] + "=" + pair[1] + "&"
    }
    if(form_data.charAt(form_data.length - 1) == '&')
        form_data = form_data.substring(0, form_data.length-1);
    let xhr = new XMLHttpRequest();
    xhr.open('POST', '/dataexchange');
    xhr.send(form_data);

    xhr.onreadystatechange = () => {    
        let result = document.getElementById("result");
        if(xhr.status == 200)
            result.innerHTML = xhr.responseText;
    }
}