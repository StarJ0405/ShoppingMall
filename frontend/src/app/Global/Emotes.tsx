import DropDown, { Direcion } from "@/app/Global/DropDown";


export const emoteList = ['😁', '😂', '🤣', '😄', '😅', '😆', '😉', '😊', '😋', '😎', '😍', '😘', '😗', '😙', '😚', '🙂', '🤗', '🤩'];
interface EmoteDropDownProps {
    open: boolean;
    setIsOpen: (v: boolean) => void;
    input_id: string;
    button: string;
    background: string;
    onClick?: (item: string) => void;
}
export function EmoteDropDown(props: EmoteDropDownProps) {

    const items = [];
    for (let i = 0; i < emoteList.length; i++) {
        const handler = () => { const input = document.getElementById(props.input_id) as HTMLInputElement; input.value = input.value + emoteList[i]; props.onClick?.(emoteList[i]); };
        items.push({ key: emoteList[i], label: emoteList[i], className: 'flex flex-wrap w-[20px]', onClick: handler });
    }

    return <>
        <DropDown open={props.open} onClose={() => props.setIsOpen(false)} className={" flex border rounded-box bg-base-100 "} width={150} height={80} defaultDriection={Direcion.UP} background={props.background} button={props.button}>
            <div className="z-[100] fixed top-0 left-0 right-0 bottom-0" onClick={() => props.setIsOpen(false)}></div>
            <ul className="z-[100] flex flex-wrap overflow-y-scroll w-full h-full">
                {items.map((item) => (
                    <li key={item.key} className={item.className}>
                        <a className="p-0 m-0 hover:bg-black" onClick={(e) => { e.preventDefault(); item.onClick(); }}>{item.label}</a>
                    </li>))}
            </ul>
        </DropDown>
    </>
};
interface ButtonProps {
    open: boolean;
    id: string;
    setIsOpen: (v: boolean) => void;
    className?: string;
}
export function EmoteButton(props: ButtonProps) {
    return <><button className={props.className} id={props.id} onClick={() => props.setIsOpen(!props.open)}>😀</button></>;
}