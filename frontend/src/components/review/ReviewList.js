const ReviewList = ({ comments }) => {
    if (comments.length === 0) {
        return <p>아직 등록된 리뷰가 없습니다.</p>
    }

    return (
        <ul>
            {comments.map((comment) => (
                <li key={comment.id}>
                    <p>{comment.id}</p>
                    <p>{comment.username}</p>
                    <p>{comment.content}</p>
                </li>
            ))}
        </ul>
    )
}
export default ReviewList; 