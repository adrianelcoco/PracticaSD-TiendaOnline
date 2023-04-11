let form = document.getElementById('form');
let name = document.getElementById('name');
let price = document.getElementById('price');
let amount = document.getElementById('amount');
form.addEventListener("submit",e=>{
    if(name.value.length < 1){
        name.classList.add('incorrectform');
        document.getElementById('name-label').classList.add('incorrectLabel');
        e.preventDefault();
        if(price.value.length < 1 && amount.value.length < 1){
            price.classList.add('incorrectform');
            document.getElementById('price-label').classList.add('incorrectLabel');
            amount.classList.add('incorrectform');
            document.getElementById('amount-label').classList.add('incorrectLabel');
            alert("Faltan todos los campos por rellenar");
        }
        else if(price.value.length < 1){
            price.classList.add('incorrectform');
            document.getElementById('price-label').classList.add('incorrectLabel');
            alert("Falta el nombre y el precio del producto");
        }
        else if(amount.value.length < 1){
            amount.classList.add('incorrectform');
            document.getElementById('price-label').classList.add('incorrectLabel');
            alert("Falta el nombre y el stock del producto");
        }
        else{
            alert("Falta el nombre del producto");
        }

    }
    else if(price.value.length < 1){
        price.classList.add('incorrectform');
        document.getElementById('price-label').classList.add('incorrectLabel');
        e.preventDefault();
        if(amount.value.length < 1){
            amount.classList.add('incorrectform');
            document.getElementById('price-label').classList.add('incorrectLabel');
            alert("Falta el precio y el stock del producto");
        }
        else{
            alert("Falta el precio del producto");
        }

    }
    else if(amount.value.length < 1){
        amount.classList.add('incorrectform');
        document.getElementById('price-label').classList.add('incorrectLabel');
        e.preventDefault();
        alert("Falta el stock del producto");
    }
    else{
        alert("Producto creado correctamente");
    }
})