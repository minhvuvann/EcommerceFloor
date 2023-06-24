
export const NumberUtils = {
    isOdd: function (number) {
        return number % 2 === 1;
    },
    cutToken: function (tokens, decimal) {
        let token = new Map();
        tokens.split(decimal).forEach(id => {
            token.set(id, true);
        })
        return token;
    }

}

export default NumberUtils;
