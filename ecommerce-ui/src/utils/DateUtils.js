import {format} from "date-fns";

export const DateUtils = {
    convert: function (date) {
        const newDate = new Date(date);
        return format(newDate, 'HH:mm:ss dd-MM-yyyy');
    }

}

export default DateUtils;