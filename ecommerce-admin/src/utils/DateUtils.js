import {format} from "date-fns";

export const DateUtils = {
    convert: function (date) {
        if (date) {
            const newDate = new Date(date);
            return format(newDate, 'HH:mm:ss dd-MM-yyyy');
        }
        return date;
    }

}

export default DateUtils;