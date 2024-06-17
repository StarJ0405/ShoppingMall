export function Move(id: string) {
    document.getElementById(id)?.focus();
}
export function KeyDownCheck({ preKey, setPreKey, e, pre, next }: { preKey: string, setPreKey: (value: any) => void, e: any, pre?: () => void, next?: () => void }) {
    if (pre && preKey != '' && e.key == 'Enter')
        pre();
    else if (next && preKey == '' && e.key == 'Enter')
        next();
    if (e.key == 'Shift')
        setPreKey('Shift');
    else if (preKey != null)
        setPreKey('');
}

export function PhoneString(phone: string) {
    return phone?.slice(0, 3) + "-" + phone?.slice(3, 7) + "-" + phone?.slice(7);
}
export function checkInput(check: any, pattern: string, True: () => void, False: () => void) {
    if (new RegExp(pattern).test(check.target.value))
        True();
    else
        False();
}
export function PhoneNumberCheck(e: any) {
    const input = e.target as HTMLInputElement;
    input.value = input.value.replace(/[^0-9]/g, '');
    if (input.value.length > 3 && input.value.charAt(3) != '-') {
        const value = input.value;
        input.value = value.slice(0, 3) + '-' + value.slice(3);
    }
    if (input.value.length > 8 && input.value.charAt(8) != '-') {
        const value = input.value;
        input.value = value.slice(0, 8) + '-' + value.slice(8);
    }
    if (input.value.length > 13)
        input.value = input.value.slice(0, 13);
    if (input.value.lastIndexOf('-') == input.value.length - 1)
        input.value = input.value.slice(0, input.value.length - 1);

}
export function Check(pattern: string, test: string) {
    return new RegExp(pattern).test(test);
}
export function MonthDate() {
    const now = new Date();
    let week = ''
    switch ((now.getDay() + 2)%7) {
        case 0: week = '일'; break;
        case 1: week = '월'; break;
        case 2: week = '화'; break;
        case 3: week = '수'; break;
        case 4: week = '목'; break;
        case 5: week = '금'; break;
        case 6: week = '토'; break;
    }
    return (now.getMonth() + 1) + '/' + (now.getDate() + 2) + '(' + week + ')';
}