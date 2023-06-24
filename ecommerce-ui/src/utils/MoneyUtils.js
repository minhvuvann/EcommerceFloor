export const MoneyUtils = {
    getMoney: function (amount) {
        let rA = "";
        const arrA = amount.toString().split('');
        let temp = 0;
        for (let i = arrA?.length - 1; i >= 0; i--) {
            temp++;
            rA += arrA[i];
            if (i !== 0 && temp === 3) {
                rA += '.';
                temp = 0;
            }
        }
        return '₫' + rA.split("").reverse().join("");
    }

}

export default MoneyUtils;
