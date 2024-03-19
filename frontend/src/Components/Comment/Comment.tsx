import React, { FC, useState } from "react";
import { formatDate } from "../../Utils/utils";
import { useLike } from "../../Hooks/useLike";
import { useComment } from "../../Hooks/useComment";
import { useDeleteComment } from "../../Hooks/useDeleteComment";
import heart from "../../img/heart.svg";
import readHeart from "../../img/redHeart.svg";
import recycleBin from "../../img/recycleBin.svg";
import "./Comments.scss";

interface IComment {
  id: number;
  username: string;
  commentText: string;
  likes: number;
  liked: boolean;
  postDate: string;
}
interface ICommentProps {
  comments: IComment[];
  id: number;
}
const Comment: FC<ICommentProps> = ({ comments, id }) => {
  const [textareaValue, setTextareaValue] = useState("");
  const handleTextAreaChange = (
    event: React.ChangeEvent<HTMLTextAreaElement>
  ) => {
    const textarea = event.target;
    setTextareaValue(event.target.value);
    textarea.style.height = "auto"; // Сначала сбрасываем высоту, чтобы правильно измерить новую высоту содержимого
    textarea.style.height = textarea.scrollHeight + "px"; // Затем устанавливаем высоту равной высоте скролла, чтобы вместить весь текст
  };
  const role = localStorage.getItem("role");
  const { mutate } = useLike();
  const { mutate: mutateComment } = useComment();
  const { mutate: mutateDeleteComment } = useDeleteComment();

  const handleLike = (commentId: number, action: "like" | "unlike") => {
    const data = { commentId: commentId, action: action };
    mutate(data);
  };

  const sendComment = () => {
    const data = { text: textareaValue, id: id };
    mutateComment(data);
    setTextareaValue("");
  };

  return (
    <>
      <div className="comments">
        <h3>{comments?.length} комментариев</h3>
        <div className="comments__sendComment">
          <textarea
            value={textareaValue}
            placeholder="Введите комментарий"
            onChange={(event) => handleTextAreaChange(event)}
          ></textarea>
          <button onClick={() => sendComment()}>Оставить комментарий</button>
        </div>
        <div className="comments__body">
          {comments &&
            comments.map((comment) => {
              return (
                <div className="comments__body-piece">
                  <div className="comments__body-header">
                    <div className="comments__body-userHeader">
                      <h2>{comment.username}</h2>
                      <h3>{formatDate(comment.postDate)}</h3>
                    </div>
                    {role === "ROLE_ADMIN" && (
                      <img
                        src={recycleBin}
                        className="comments__recycleBin"
                        width="30px"
                        onClick={() => mutateDeleteComment(comment.id)}
                      />
                    )}
                  </div>

                  <p>{comment.commentText}</p>
                  <div className="comments__body-like">
                    {comment.liked ? (
                      <img
                        src={readHeart}
                        width="22px"
                        onClick={() => handleLike(comment.id, "unlike")}
                      />
                    ) : (
                      <img
                        src={heart}
                        width="22px"
                        onClick={() => handleLike(comment.id, "like")}
                      />
                    )}
                    <h2>{comment.likes}</h2>
                  </div>
                </div>
              );
            })}
        </div>
      </div>
    </>
  );
};

export default Comment;
