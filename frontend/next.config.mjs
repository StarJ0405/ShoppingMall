/** @type {import('next').NextConfig} */
const nextConfig = {
	reactStrictMode: true,
	eslint: {
		ignoreDuringBuilds: true,
	},
	async rewrites() {
		return [
			{
				source: '/api/:path*',
				destination: 'http://localhost:8080/api/:path*',
			},
			{
				source: '/ws-stomp',
				destination: 'http://localhost:8080/ws-stomp',
			},
			{
				source: '/ws-stomp/',
				destination: 'http://localhost:8080/ws-stomp/',
			},
			{
				source: '/ws-stomp/:path*',
				destination: 'http://localhost:8080/ws-stomp/:path*',
			}
		]
	},
	trailingSlash: true
};

export default nextConfig;
