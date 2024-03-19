import loading from "../../img/loading.svg";
import "./Loading.scss";
const LoadingModal = () => {
  return (
    <div className="loading">
      <h1>Data is loading</h1>
      <img src={loading} alt="" />
    </div>
  );
};

export default LoadingModal;
