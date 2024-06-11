

export default function Page({ params }: { params: any }) {
    const id = params.id;
    return <div>{id}</div>;
}