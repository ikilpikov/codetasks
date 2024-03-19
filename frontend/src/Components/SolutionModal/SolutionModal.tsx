import { FC } from "react";
import { useSolutionStore } from "../../store";
import { useNavigate } from "react-router-dom";

interface ISolutionInfoProps {
  taskId: string | undefined;
}
import "./SolutionModal.scss";
const SolutionInfo: FC<ISolutionInfoProps> = ({ taskId }) => {
  const { result, executeTime, setVisible } = useSolutionStore();
  const navigator = useNavigate();
  const solutionClick = () => {
    setVisible();
    if (executeTime) navigator(`/solution/${taskId}`);
  };
  return (
    <div className="solutionInfo">
      <h3>{result}</h3>
      {!!executeTime && <h4>Execute time : {executeTime}</h4>}
      <button onClick={solutionClick}>Ok</button>
    </div>
  );
};

export default SolutionInfo;
