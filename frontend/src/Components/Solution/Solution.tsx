import { FC } from "react";
import { ISolution } from "../../Services/services";
import { formatDate } from "../../Utils/utils";
import "./Solution.scss";
const Solution: FC<ISolution> = ({
  id,
  username,
  submissionDate,
  submissionTime,
  code,
}) => {
  return (
    <div className="solution">
      <div>
        <h3>{username}</h3>
        <h4>{formatDate(submissionDate)}</h4>
        <span>{"Execute time: " + submissionTime + "ms"}</span>
      </div>

      <textarea>{code}</textarea>
    </div>
  );
};

export default Solution;
