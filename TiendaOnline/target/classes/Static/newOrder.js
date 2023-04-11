let form = document.getElementById('form');
let name = document.getElementById('name');
let street = document.getElementById('street');

form.addEventListener("submit",e=>{
    if(name.value.length <1){
        e.preventDefault();
        name.classList.add('incorrectform');
        document.getElementById('name-label').classList.add('incorrectLabel');
        if(street.value.length <1){
            street.classList.add('incorrectform');
            document.getElementById('street-label').classList.add('incorrectLabel');
            alert("Falta el nombre y la calle del comprador");
        }
        else{
            alert("Falta el nombre del comprador");
        }
    }
    else if(street.value.length <1){
        e.preventDefault();
        street.classList.add('incorrectform');
        document.getElementById('street-label').classList.add('incorrectLabel');
        alert("Falta la calle del comprador");
    }
})