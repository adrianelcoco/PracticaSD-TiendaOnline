let forms = document.querySelectorAll('#form');

forms.forEach(f => {
    let reduceButton = f.querySelector('#reduceButton');

    reduceButton.addEventListener('click', e => {
        let stockLabel = f.querySelector('#stock-label');
        let currentStock = parseInt(stockLabel.textContent);
        let newStockInput = f.querySelector('#newStock');
        let newStock = parseInt(newStockInput.value);
        let result = currentStock - newStock;
        if (result < 0) {
            alert('El stock resultante no puede ser menor que 0');
            e.preventDefault();
        }
    });
});
