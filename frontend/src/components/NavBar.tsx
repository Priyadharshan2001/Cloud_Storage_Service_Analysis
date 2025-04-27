import { Link } from "react-router";

const NavBar = () => {
  return (
    <div className="fixed top-0 left-0 h-screen w-[250px] bg-gray-800 text-white p-5 flex flex-col gap-6 shadow-lg">
      <h2 className="text-2xl font-bold mb-5">Menu</h2>
      <nav className="flex flex-col gap-4">
        <Link to="/" className="flex items-center gap-3 text-lg p-3 rounded bg-gray-700 hover:bg-gray-600 w-full">
          🏠 Home
        </Link>
        <Link to="/search" className="flex items-center gap-3 text-lg p-3 rounded bg-gray-700 hover:bg-gray-600 w-full">
          🔍 Search
        </Link>
        <Link to="/search_history" className="flex items-center gap-3 text-lg p-3 rounded bg-gray-700 hover:bg-gray-600 w-full">
          📜 Search History
        </Link>
        <Link to="/Subscribe" className="flex items-center gap-3 text-lg p-3 rounded bg-gray-700  hover:bg-gray-600 w-full">
          ✉️ Subscribe
        </Link>
        <Link to="/Aboutus" className="flex items-center gap-3 text-lg p-3 rounded bg-gray-700 hover:bg-gray-700 transition-colors w-full">
          ℹ️ AboutUs
        </Link>
        {/* <Link to="/frequency" className="flex items-center gap-3 text-lg p-3 rounded bg-gray-700 hover:bg-gray-600 w-full">
          📊 Frequency Count
        </Link> */}
      </nav>
    </div>
  );
};

export default NavBar;
