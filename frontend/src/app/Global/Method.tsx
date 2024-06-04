export function Move(id:string){
    document.getElementById(id)?.focus();
}
export function KeyDownCheck({preKey, setPreKey, e, pre,next}:{preKey:string,setPreKey:(value:any)=>void, e:any,pre?:()=>void,next?:()=>void}){
    console.log(preKey,e.key);
    if(pre&& preKey!=''&& e.key=='Enter')
        pre();
    else if(next && preKey==''&& e.key=='Enter')
        next();
    if(e.key=='Shift')
        setPreKey('Shift');
    else if(preKey!=null)
        setPreKey('');
}