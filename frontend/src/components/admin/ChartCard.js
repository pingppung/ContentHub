import { BarChart, Bar, XAxis, YAxis, Tooltip, Legend, PieChart, Pie, Cell, ResponsiveContainer } from 'recharts';

const COLORS = ['#A2AADB', '#82ca9d', '#ffc658', '#ff8042', '#8dd1e1', '#a4de6c'];

export default function ChartCard({ title, data, type = 'bar', xKey = 'field' }) {
  return (
    <div className="rounded-2xl shadow p-4 w-full max-w-2xl bg-white mb-6">
      <h3 className="text-xl font-semibold mb-4">{title}</h3>
      <div className="rounded-2xl shadow p-4 w-full max-w-2xl bg-white mb-6">
        <h3 className="text-xl font-semibold mb-4">{title}</h3>

        {type === 'bar' && (
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={data}>
              <XAxis dataKey={xKey} />
              <YAxis />
              <Tooltip />
              <Legend />
              <Bar dataKey="count" fill={COLORS[0]} />
            </BarChart>
          </ResponsiveContainer>
        )}

        {type === 'pie' && (
          <ResponsiveContainer width="100%" height={300}>
            <PieChart>
              <Pie
                data={data}
                dataKey="count"
                nameKey={xKey}
                cx="50%"
                cy="50%"
                outerRadius={100}
                label={(entry) => `${entry[xKey]} ${(entry.percent * 100).toFixed(0)}%`}
              >
                {data.map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                ))}
              </Pie>
              <Tooltip />
            </PieChart>
          </ResponsiveContainer>
        )}
      </div>

    </div>
  );
}
