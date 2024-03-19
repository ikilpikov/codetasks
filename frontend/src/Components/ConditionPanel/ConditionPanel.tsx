import { useSandBoxStore } from "../../store";
const ConditionPanel = () => {
  const { condition } = useSandBoxStore();
  return (
    <div className="conditionalPanel">
      <h3>Condition</h3>
      <p>{condition}</p>
    </div>
  );
};

export default ConditionPanel;
